package com.example.satommvm.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "fotos_carro",
    foreignKeys = [ForeignKey(
        entity = Carro::class,
        parentColumns = ["id"],
        childColumns = ["carroId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class FotoCarro(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val carroId: Int,
    val imagemUri: String
)