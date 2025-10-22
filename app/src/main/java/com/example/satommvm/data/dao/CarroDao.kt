package com.example.satommvm.data.dao

import androidx.room.*
import com.example.satommvm.data.model.Carro
import kotlinx.coroutines.flow.Flow

@Dao
interface CarroDao {

    @Query("SELECT * FROM carros")
    fun getCarros(): Flow<List<Carro>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(carro: Carro)

    @Update
    suspend fun update(carro: Carro)

    @Delete
    suspend fun delete(carro: Carro)

    @Query("SELECT * FROM carros WHERE placa = :placa LIMIT 1")
    fun getCarroByPlaca(placa: String): Flow<Carro?>
}
