package com.example.aula09_09.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.satommvm.data.repository.UsuarioRepository
import com.example.satommvm.ui.viewmodel.UsuarioViewModel

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String,
    val senha: String
)
val fakeUsuarioDao = object : UsuarioDao {
    override suspend fun insert(usuario: Usuario) {}
    override suspend fun login(nome: String, senha: String): Usuario? {
        return if (nome == "Lucas" && senha == "123") Usuario(nome = "Lucas", senha = "123") else null
    }
    override fun getUsuarios() = kotlinx.coroutines.flow.flowOf(emptyList<Usuario>())
}

val fakeUsuarioRepository = UsuarioRepository(fakeUsuarioDao)
val fakeUsuarioViewModel = UsuarioViewModel(fakeUsuarioRepository)