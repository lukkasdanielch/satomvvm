package com.example.satommvm.ui.cadastro_carro

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.satommvm.data.model.Carro
import com.example.satommvm.data.repository.CarroRepository
import com.example.satommvm.ui.viewmodel.CadastroCarroUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

class CadastroCarroViewModel(private val repository: CarroRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(CadastroCarroUiState())
    val uiState: StateFlow<CadastroCarroUiState> = _uiState.asStateFlow()

    // --- Funções de Evento para CadastroCarro ---
    fun updateCadastroNome(nome: String) { _uiState.update { it.copy(nome = nome) } }
    fun updateCadastroModelo(modelo: String) { _uiState.update { it.copy(modelo = modelo) } }
    fun updateCadastroAno(ano: String) { _uiState.update { it.copy(ano = ano) } }
    fun updateCadastroPlaca(placa: String) { _uiState.update { it.copy(placa = placa) } }
    fun updateCadastroImagemUri(uri: String?) { _uiState.update { it.copy(imagemUri = uri) } }

    fun salvarCarro(context: Context) {
        val uiState = _uiState.value

        if (uiState.nome.isBlank() || uiState.modelo.isBlank() || uiState.ano.isBlank() || uiState.placa.isBlank() || uiState.imagemUri == null) {
            _uiState.update { it.copy(errorMessage = "Preencha todos os campos e adicione uma imagem!") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val caminhoImagem = saveImageToInternalStorage(
                    context,
                    Uri.parse(uiState.imagemUri),
                    "carro_${uiState.placa}.jpg"
                )
                val novoCarro = Carro(
                    nome = uiState.nome,
                    modelo = uiState.modelo,
                    ano = uiState.ano.toIntOrNull() ?: 0,
                    placa = uiState.placa,
                    imagemUri = caminhoImagem
                )
                repository.insert(novoCarro)
                _uiState.update { it.copy(isLoading = false, isSaveSuccess = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Falha ao salvar: ${e.message}") }
            }
        }
    }

    fun resetCadastroCarroState() {
        _uiState.update { CadastroCarroUiState() }
    }

    private fun saveImageToInternalStorage(context: Context, uri: Uri, fileName: String): String {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.filesDir, fileName)
        inputStream.use { input ->
            file.outputStream().use { output ->
                input?.copyTo(output)
            }
        }
        return file.absolutePath
    }
}