package com.example.satommvm.ui.viewmodel

data class AlterarCarroUiState(
    val id: Int = 0,
    val nome: String = "",
    val modelo: String = "",
    val ano: String = "",
    val placa: String = "",
    val imagemUri: String? = null,
    val isLoading: Boolean = true,
    val updateSuccess: Boolean = false,
    val errorMessage: String? = null
)