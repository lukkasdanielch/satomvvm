package com.example.satommvm.ui.foto_carro

import com.example.satommvm.data.model.FotoCarro

data class FotoCarroUiState(
    val fotos: List<FotoCarro> = emptyList(),
    val isLoading: Boolean = true,
    val isAddingFoto: Boolean = false,
    val errorMessage: String? = null
)