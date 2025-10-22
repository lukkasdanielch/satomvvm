package com.example.satommvm.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aula09_09.data.CarroDao
import com.example.satommvm.data.model.Carro
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class CarroViewModel(private val repository: CarroDao) : ViewModel() {

    private val _carros = MutableStateFlow<List<Carro>>(emptyList())
    val carros: StateFlow<List<Carro>> = _carros

    init {
        carregarCarros()
    }

    fun carregarCarros() {
        viewModelScope.launch {
            repository.getCarros().collect { lista ->
                _carros.value = lista
            }
        }
    }

    fun deletar(carro: Carro) {
        viewModelScope.launch {
            repository.delete(carro)
            carregarCarros()
        }
    }
}
