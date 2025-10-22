package com.example.satommvm.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.satommvm.data.model.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(usuario: Usuario)

    @Query("SELECT * FROM usuario WHERE nome = :nome AND senha = :senha LIMIT 1")
    suspend fun login(nome: String, senha: String): Usuario?

    @Query("SELECT * FROM usuario")
    fun getUsuarios(): Flow<List<Usuario>>
}
