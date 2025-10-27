package com.example.satommvm.ui.login // Verifique o seu package

import com.example.satommvm.data.model.Usuario

data class LoginUiState(
    val username: String = "",
    val password: String = "",

    val isLoading: Boolean = false,
    val loginSuccess: Usuario? = null,
    val errorMessage: String? = null
)