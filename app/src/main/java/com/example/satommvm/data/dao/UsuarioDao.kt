package com.example.aula09_09.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {

    @Insert
    suspend fun insert(usuario: Usuario)

    @Query("SELECT * FROM usuarios WHERE nome = :nome AND senha = :senha")
    suspend fun login(nome: String, senha: String): Usuario?

    @Query("SELECT * FROM usuarios")
    fun getUsuarios(): Flow<List<Usuario>>
}
