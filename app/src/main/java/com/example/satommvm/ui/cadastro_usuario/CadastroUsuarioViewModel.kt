package com.example.satommvm.ui.cadastro_usuario

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.satommvm.data.model.Usuario
import com.example.satommvm.data.repository.UsuarioRepository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CadastroUsuarioViewModel(private val repository: UsuarioRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(CadastroUsuarioUiState())
    val uiState: StateFlow<CadastroUsuarioUiState> = _uiState.asStateFlow()

    fun updateUsername(username: String) {
        _uiState.update { it.copy(username = username, errorMessage = null) }
    }

    fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password, errorMessage = null) }
    }

    fun cadastrar() {
        if (uiState.value.username.isBlank() || uiState.value.password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Preencha todos os campos!") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                repository.insert(Usuario(nome = uiState.value.username, senha = uiState.value.password))
                _uiState.update { it.copy(
                    isLoading = false,
                    registerSuccess = true
                )}
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message ?: "Erro ao registar") }
            }
        }
    }

    fun resetRegisterState() {
        _uiState.update { it.copy(registerSuccess = false, errorMessage = null) }
    }
}