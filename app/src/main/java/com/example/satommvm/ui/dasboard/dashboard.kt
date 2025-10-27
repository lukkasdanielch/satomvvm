package com.example.satommvm.ui.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable // <-- IMPORTAR
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.satommvm.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dashboard(
    nome: String,
    navController: NavHostController,
    dashboardViewModel: DashboardViewModel
) {
    val uiState by dashboardViewModel.uiState.collectAsState()

    Scaffold(
        // ... (Scaffold, TopBar, FAB e Barra de Pesquisa como antes) ...
        containerColor = Color.Black,
        topBar = { TopAppBar(title = { Text("Gestão de Carros - Bem-vindo, $nome!") }) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("cadastroCarro") },
                containerColor = Color(0xFFAA162C),
                contentColor = Color.White
            ) { Text("+") }
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp)
        ) {

            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = { dashboardViewModel.updateSearchQuery(it) },
                label = { Text("Pesquisar por nome, modelo ou placa...") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                singleLine = true
            )

            // ... (Lógica de Loading e Lista Vazia) ...
            if (uiState.isLoading) { /*...*/ }
            else if (uiState.filteredCarros.isEmpty()) { /*...*/ }
            else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.filteredCarros) { carro ->
                        Card(
                            // <-- CORREÇÃO: O Card agora é clicável
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    // Navega para a tela de fotos com o ID do carro
                                    navController.navigate("fotoCarro/${carro.id}")
                                },
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Nome: ${carro.nome}", style = MaterialTheme.typography.titleMedium)
                                    Text("Modelo: ${carro.modelo}")
                                    Text("Ano: ${carro.ano}")
                                    Text("Placa: ${carro.placa}")
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Image(
                                    painter = carro.imagemUri?.let { rememberAsyncImagePainter(it) }
                                        ?: carro.imagemRes?.let { painterResource(id = it) }
                                        ?: painterResource(id = R.drawable.sato),
                                    contentDescription = carro.nome,
                                    modifier = Modifier.size(100.dp), // Removido o clickable da imagem
                                    contentScale = ContentScale.Crop
                                )
                            }

                            // --- Botões (Editar e Deletar) ---
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                Button(
                                    onClick = { navController.navigate("alterarCarro/${carro.placa}") },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0277BD), contentColor = Color.White)
                                ) { Text("Editar") }

                                Spacer(modifier = Modifier.width(8.dp))

                                Button(
                                    onClick = { dashboardViewModel.deleteCarro(carro) },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAA162C), contentColor = Color.White)
                                ) { Text("Deletar") }
                            }
                        }
                    }
                }
            }
        }
    }
}