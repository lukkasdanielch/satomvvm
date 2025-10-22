package com.example.satommvm.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aula09_09.data.Usuario
import com.example.aula09_09.data.UsuarioDao
import com.example.satommvm.data.repository.UsuarioRepository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UsuarioViewModel(private val repository: UsuarioRepository) : ViewModel() {

    private val _usuarios = MutableStateFlow<List<Usuario>>(emptyList())
    val usuarios: StateFlow<List<Usuario>> = _usuarios

    fun login(nome: String, senha: String, onResult: (Usuario?) -> Unit) {
        viewModelScope.launch {
            val user = repository.login(nome, senha)
            onResult(user)
        }
    }

    fun cadastrar(nome: String, senha: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            val novoUsuario = Usuario(nome = nome, senha = senha)
            repository.insert(novoUsuario)
            onComplete()
        }
    }
}
