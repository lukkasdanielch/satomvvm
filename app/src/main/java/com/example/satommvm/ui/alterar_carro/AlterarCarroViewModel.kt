package com.example.satommvm.ui.alterar_carro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.satommvm.data.model.Carro
import com.example.satommvm.data.repository.CarroRepository
import com.example.satommvm.ui.viewmodel.AlterarCarroUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AlterarCarroViewModel(private val repository: CarroRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AlterarCarroUiState())
    val uiState: StateFlow<AlterarCarroUiState> = _uiState.asStateFlow()

    fun carregarCarro(placa: String) {
        viewModelScope.launch {
            repository.getCarroByPlaca(placa)
                .catch { _uiState.update { it.copy(isLoading = false, errorMessage = "Falha ao carregar carro") } }
                .collect { carro ->
                    if (carro != null) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                id = carro.id,
                                nome = carro.nome,
                                modelo = carro.modelo,
                                ano = carro.ano.toString(),
                                placa = carro.placa,
                                imagemUri = carro.imagemUri
                            )
                        }
                    } else {
                        _uiState.update { it.copy(isLoading = false, errorMessage = "Carro não encontrado") }
                    }
                }
        }
    }

    // --- Funções de Evento ---
    fun updateNome(nome: String) { _uiState.update { it.copy(nome = nome) } }
    fun updateModelo(modelo: String) { _uiState.update { it.copy(modelo = modelo) } }
    fun updateAno(ano: String) { _uiState.update { it.copy(ano = ano) } }
    fun updateImagemUri(uri: String?) { _uiState.update { it.copy(imagemUri = uri) } }

    fun salvarAlteracoes() {
        val uiState = _uiState.value
        val carroAtualizado = Carro(
            id = uiState.id,
            nome = uiState.nome,
            modelo = uiState.modelo,
            ano = uiState.ano.toIntOrNull() ?: 0,
            placa = uiState.placa,
            imagemUri = uiState.imagemUri
        )

        viewModelScope.launch {
            try {
                repository.update(carroAtualizado)
                _uiState.update { it.copy(updateSuccess = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Falha ao atualizar: ${e.message}") }
            }
        }
    }

    fun resetAlterarCarroState() {
        _uiState.update { it.copy(updateSuccess = false, errorMessage = null) }
    }
}