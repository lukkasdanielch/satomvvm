package com.example.satommvm.data.dao

import androidx.room.* // <-- Importar 'Delete'
import com.example.satommvm.data.model.FotoCarro
import kotlinx.coroutines.flow.Flow

@Dao
interface FotoCarroDao {

    @Query("SELECT * FROM fotos_carro WHERE carroId = :carroId")
    fun getFotos(carroId: Int): Flow<List<FotoCarro>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(foto: FotoCarro)

    @Delete // <-- ADICIONE ESTA FUNÇÃO
    suspend fun delete(foto: FotoCarro)
}