package com.example.satommvm.data.repository

import com.example.satommvm.data.dao.CarroDao
import com.example.satommvm.data.model.Carro
import kotlinx.coroutines.flow.Flow

class CarroRepository(private val carroDao: CarroDao) {
    fun getCarros(): Flow<List<Carro>> = carroDao.getCarros()

    // <-- ADICIONAR ESTA LINHA
    fun getCarroByPlaca(placa: String): Flow<Carro?> = carroDao.getCarroByPlaca(placa)

    suspend fun insert(carro: Carro) = carroDao.insert(carro)
    suspend fun update(carro: Carro) = carroDao.update(carro)
    suspend fun delete(carro: Carro) = carroDao.delete(carro)
}