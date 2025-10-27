package com.example.satommvm.ui.cadastro_usuario

data class CadastroUsuarioUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val registerSuccess: Boolean = false,
    val errorMessage: String? = null
)