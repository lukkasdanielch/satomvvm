package com.example.satommvm.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.satommvm.data.model.Usuario
import com.example.satommvm.data.repository.UsuarioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class UsuarioViewModel(private val repository: UsuarioRepository) : ViewModel() {

    val usuarios: Flow<List<Usuario>> = repository.getUsuarios()

    fun login(nome: String, senha: String, onResult: (Usuario?) -> Unit) {
        viewModelScope.launch {
            val user = repository.login(nome, senha)
            onResult(user)
        }
    }

    fun cadastrar(nome: String, senha: String) {
        viewModelScope.launch {
            repository.insert(Usuario(nome = nome, senha = senha))
        }
    }
}
