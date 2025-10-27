package com.example.satommvm.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.satommvm.data.repository.UsuarioRepository
import com.example.satommvm.ui.login.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UsuarioRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun updateUsername(username: String) {
        _uiState.update { it.copy(username = username, errorMessage = null) }
    }

    fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password, errorMessage = null) }
    }


    fun login() {
        if (uiState.value.username.isBlank() || uiState.value.password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Preencha todos os campos!") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val user = repository.login(uiState.value.username, uiState.value.password)
                if (user != null) {
                    _uiState.update { it.copy(isLoading = false, loginSuccess = user) }
                } else {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Login inválido!") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message ?: "Erro desconhecido") }
            }
        }
    }

    fun resetLoginState() {
        _uiState.update { it.copy(loginSuccess = null, errorMessage = null) }
    }
}