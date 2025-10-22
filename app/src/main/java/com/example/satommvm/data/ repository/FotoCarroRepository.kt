package com.example.satommvm.data.repository

import com.example.satommvm.data.dao.FotoCarroDao
import com.example.satommvm.data.model.FotoCarro
import kotlinx.coroutines.flow.Flow

class FotoCarroRepository(private val dao: FotoCarroDao) {

    fun getFotosPorCarro(carroId: Int): Flow<List<FotoCarro>> = dao.getFotos(carroId)

    suspend fun adicionarFoto(foto: FotoCarro) = dao.insert(foto)
}