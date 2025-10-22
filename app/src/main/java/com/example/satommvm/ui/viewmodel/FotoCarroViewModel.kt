package com.example.satommvm.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.satommvm.data.model.FotoCarro
import com.example.satommvm.data.repository.FotoCarroRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FotoCarroViewModel(private val repository: FotoCarroRepository) : ViewModel() {

    private val _fotos = MutableStateFlow<List<FotoCarro>>(emptyList())
    val fotos: StateFlow<List<FotoCarro>> = _fotos

    fun carregarFotos(carroId: Int) {
        viewModelScope.launch {
            repository.getFotosPorCarro(carroId).collect {
                _fotos.value = it
            }
        }
    }

    fun adicionarFoto(foto: FotoCarro) {
        viewModelScope.launch {
            repository.adicionarFoto(foto)
        }
    }
}
