package com.example.satommvm.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.satommvm.data.model.FotoCarro
import com.example.satommvm.data.repository.FotoCarroRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FotoCarroViewModel(private val repository: FotoCarroRepository) : ViewModel() {

    private val _fotos = MutableStateFlow<List<FotoCarro>>(emptyList())
    val fotos: StateFlow<List<FotoCarro>> = _fotos

    // Carrega fotos de um carro específico e atualiza o StateFlow
    fun carregarFotos(carroId: Int) {
        viewModelScope.launch {
            repository.getFotosPorCarro(carroId).collectLatest { lista ->
                _fotos.value = lista
            }
        }
    }

    // Adiciona uma nova foto
    fun adicionarFoto(foto: FotoCarro, onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            repository.adicionarFoto(foto)
            onComplete?.invoke()
        }
    }
}
