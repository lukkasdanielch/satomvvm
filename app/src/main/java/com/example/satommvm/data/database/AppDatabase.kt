package com.example.satommvm.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.satommvm.data.model.Carro
import com.example.satommvm.data.model.Usuario
import com.example.satommvm.data.model.FotoCarro // <-- ADICIONAR
import com.example.satommvm.data.dao.CarroDao
import com.example.satommvm.data.dao.UsuarioDao
import com.example.satommvm.data.dao.FotoCarroDao // <-- ADICIONAR

@Database(entities = [Carro::class, Usuario::class, FotoCarro::class], version = 1) // <-- ADICIONAR
abstract class AppDatabase : RoomDatabase() {

    abstract fun carroDao(): CarroDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun fotoCarroDao(): FotoCarroDao // <-- ADICIONAR

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sato_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}