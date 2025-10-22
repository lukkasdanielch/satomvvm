package com.example.satommvm.data.repository

import com.example.aula09_09.data.CarroDao

import com.example.satommvm.data.model.Carro
import kotlinx.coroutines.flow.Flow

class CarroRepository(private val dao: CarroDao) {

    fun getCarros(): Flow<List<Carro>> = dao.getCarros()

    suspend fun delete(carro: Carro) = dao.delete(carro)
}
