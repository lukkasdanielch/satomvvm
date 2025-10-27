package com.example.satommvm.ui.dashboard

import com.example.satommvm.data.model.Carro

data class DashboardUiState(
    val isLoading: Boolean = true,
    val searchQuery: String = "",
    private val fullCarList: List<Carro> = emptyList() // Lista principal vinda do DB
) {
    /**
     * Esta é a lista que a UI vai mostrar.
     * Ela é calculada automaticamente com base na 'searchQuery'.
     */
    val filteredCarros: List<Carro>
        get() = if (searchQuery.isBlank()) {
            fullCarList
        } else {
            fullCarList.filter { carro ->
                // Pesquisa no nome, modelo ou placa (ignora maiúsculas/minúsculas)
                carro.nome.contains(searchQuery, ignoreCase = true) ||
                        carro.modelo.contains(searchQuery, ignoreCase = true) ||
                        carro.placa.contains(searchQuery, ignoreCase = true)
            }
        }

    /**
     * Função auxiliar para o ViewModel atualizar a lista principal.
     */
    fun withCarList(list: List<Carro>) = this.copy(fullCarList = list)
}