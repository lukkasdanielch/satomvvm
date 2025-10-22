package com.example.satommvm.data.repository


import com.example.aula09_09.data.Usuario
import com.example.aula09_09.data.UsuarioDao


class UsuarioRepository(private val dao: UsuarioDao) {

    suspend fun login(nome: String, senha: String): Usuario? {
        return dao.login(nome, senha)
    }

    suspend fun insert(usuario: Usuario) {
        dao.insert(usuario)
    }
}
