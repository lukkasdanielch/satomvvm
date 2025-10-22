package com.example.satommvm.data.repository



import com.example.satommvm.data.dao.UsuarioDao
import com.example.satommvm.data.model.Usuario

class UsuarioRepository(private val usuarioDao: UsuarioDao) {

    suspend fun insert(usuario: Usuario) {
        usuarioDao.insert(usuario)
    }

    suspend fun login(nome: String, senha: String): Usuario? {
        return usuarioDao.login(nome, senha)
    }

    fun getUsuarios() = usuarioDao.getUsuarios()
}