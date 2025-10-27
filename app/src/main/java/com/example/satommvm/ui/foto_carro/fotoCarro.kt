package com.example.satommvm.ui.foto_carro

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.satommvm.data.model.FotoCarro

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FotoCarro(
    navController: NavHostController,
    fotoCarroViewModel: FotoCarroViewModel,
    carroId: Int // Recebe o ID do carro da navegação
) {
    val uiState by fotoCarroViewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Launcher para Adicionar Foto
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        fotoCarroViewModel.adicionarFoto(context.applicationContext, carroId, uri?.toString())
    }

    // Carrega as fotos assim que a tela abre
    LaunchedEffect(key1 = carroId) {
        fotoCarroViewModel.carregarFotos(carroId)
    }

    // Observa erros
    LaunchedEffect(key1 = uiState.errorMessage) {
        uiState.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            fotoCarroViewModel.resetErrorState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Galeria de Fotos", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFAA162C)),
                navigationIcon = {
                    Button(onClick = { navController.popBackStack() }) { Text("Voltar") }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { launcher.launch("image/*") }, // Botão Adicionar
                containerColor = Color(0xFFAA162C),
                contentColor = Color.White
            ) {
                if (uiState.isAddingFoto) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("+") // Botão Adicionar
                }
            }
        }
    ) { padding ->
        when {
            uiState.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            // Mostrar grelha de fotos
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Se estiver vazia, mostra uma mensagem
                    if (uiState.fotos.isEmpty()) {
                        item { // Usar 'item' para ocupar a largura total
                            Text(
                                "Nenhuma foto. Adicione a primeira!",
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                    // Renderiza cada foto
                    items(uiState.fotos) { foto ->
                        FotoCard(
                            foto = foto,
                            onDeleteClick = { fotoCarroViewModel.deletarFoto(foto) }
                        )
                    }
                }
            }
        }
    }
}

// Composable privado para o Card da Foto
@Composable
private fun FotoCard(foto: FotoCarro, onDeleteClick: () -> Unit) {
    Card(elevation = CardDefaults.cardElevation(4.dp)) {
        Column {
            Image(
                painter = rememberAsyncImagePainter(foto.imagemUri),
                contentDescription = "Foto do carro",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f), // Imagem quadrada
                contentScale = ContentScale.Crop
            )
            // Botão Deletar em cada foto
            Button(
                onClick = onDeleteClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.DarkGray, // Cor discreta
                    contentColor = Color.White
                ),
                shape = MaterialTheme.shapes.small // Cantos retos
            ) {
                Text("Deletar")
            }
        }
    }
}