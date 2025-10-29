package com.example.satommvm.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.satommvm.data.model.Carro
import com.example.satommvm.data.repository.CarroRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel(private val repository: CarroRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        carregarCarros()
    }

    private fun carregarCarros() {
        viewModelScope.launch {
            repository.getCarros()
                .catch {
                    _uiState.update { it.copy(isLoading = false) }
                    // TODO: Expor um erro para a UI
                }
                .collect { listaDeCarros ->
                    _uiState.update {

                        it.withCarList(listaDeCarros).copy(isLoading = false)
                    }
                }
        }
    }

    /**
     * Nova função chamada pela View sempre que o texto da pesquisa muda.
     */
    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun deleteCarro(carro: Carro) {
        viewModelScope.launch {
            try {
                repository.delete(carro)

            } catch (e: Exception) {
                // TODO: Expor um erro para a UI
            }
        }
    }
}