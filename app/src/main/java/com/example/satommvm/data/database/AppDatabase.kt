package com.example.satommvm.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.satommvm.data.model.Carro
import com.example.satommvm.data.model.Usuario
import com.example.satommvm.data.dao.CarroDao
import com.example.satommvm.data.dao.UsuarioDao

@Database(entities = [Carro::class, Usuario::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun carroDao(): CarroDao
    abstract fun usuarioDao(): UsuarioDao

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
