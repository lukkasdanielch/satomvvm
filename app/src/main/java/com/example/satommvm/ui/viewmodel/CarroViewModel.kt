package com.example.satommvm.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.satommvm.data.model.Carro
import com.example.satommvm.data.repository.CarroRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CarroViewModel(private val repository: CarroRepository) : ViewModel() {

    // Flow que observa todos os carros
    val carros: Flow<List<Carro>> = repository.getCarros()

    fun addCarro(carro: Carro, onFinish: () -> Unit) {
        viewModelScope.launch {
            repository.insert(carro)
            onFinish()
        }
    }

    fun updateCarro(carro: Carro) = viewModelScope.launch {
        repository.update(carro)
    }

    fun deleteCarro(carro: Carro) = viewModelScope.launch {
        repository.delete(carro)
    }
}
