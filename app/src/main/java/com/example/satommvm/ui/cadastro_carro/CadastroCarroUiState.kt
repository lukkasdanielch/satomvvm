package com.example.satommvm.ui.viewmodel

data class CadastroCarroUiState(
    val nome: String = "",
    val modelo: String = "",
    val ano: String = "",
    val placa: String = "",
    val imagemUri: String? = null,
    val isLoading: Boolean = false,
    val isSaveSuccess: Boolean = false,
    val errorMessage: String? = null
)