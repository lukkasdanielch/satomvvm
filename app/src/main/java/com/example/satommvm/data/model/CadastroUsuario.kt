

package com.example.satommvm.ui.viewmodel

data class CadastroUsuarioUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val registerSuccess: Boolean = false,
    val errorMessage: String? = null
)