package com.example.satommvm.ui.alterar_carro

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlterarCarro(
    navController: NavHostController,
    placa: String,
    alterarCarroViewModel: AlterarCarroViewModel // <-- CORREÇÃO
) {
    val uiState by alterarCarroViewModel.uiState.collectAsState()
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        alterarCarroViewModel.updateImagemUri(uri?.toString())
    }

    LaunchedEffect(key1 = placa) {
        alterarCarroViewModel.carregarCarro(placa)
    }

    LaunchedEffect(key1 = uiState) {
        if (uiState.updateSuccess) {
            Toast.makeText(context, "Carro atualizado com sucesso!", Toast.LENGTH_SHORT).show()
            alterarCarroViewModel.resetAlterarCarroState()
            navController.popBackStack()
        }
        uiState.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            alterarCarroViewModel.resetAlterarCarroState()
        }
    }

    Scaffold(
        containerColor = Color.Black,
        topBar = {
            TopAppBar(
                title = { Text("Alterar Carro", color = Color.White) },
                navigationIcon = {
                    Button(
                        onClick = { navController.popBackStack() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFAA162C),
                            contentColor = Color.White
                        )
                    ) { Text("Voltar") }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        }
    ) { padding ->

        when {
            uiState.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.White)
                }
            }
            uiState.errorMessage != null && uiState.id == 0 -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(uiState.errorMessage ?: "Erro", color = Color.White)
                }
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(uiState.imagemUri ?: ""),
                        contentDescription = "Imagem do carro",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clickable { launcher.launch("image/*") },
                        contentScale = ContentScale.Crop
                    )
                    Button(
                        onClick = { launcher.launch("image/*") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFAA162C),
                            contentColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Selecionar/Alterar Imagem") }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
                    ) {
                        TextField(
                            value = uiState.nome,
                            onValueChange = { alterarCarroViewModel.updateNome(it) },
                            label = { Text("Nome", color = Color.White) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.DarkGray,
                                unfocusedContainerColor = Color.DarkGray,
                                cursorColor = Color.White
                            )
                        )
                    }
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
                    ) {
                        TextField(
                            value = uiState.modelo,
                            onValueChange = { alterarCarroViewModel.updateModelo(it) },
                            label = { Text("Modelo", color = Color.White) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.DarkGray,
                                unfocusedContainerColor = Color.DarkGray,
                                cursorColor = Color.White
                            )
                        )
                    }
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
                    ) {
                        TextField(
                            value = uiState.ano,
                            onValueChange = { alterarCarroViewModel.updateAno(it) },
                            label = { Text("Ano", color = Color.White) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.DarkGray,
                                unfocusedContainerColor = Color.DarkGray,
                                cursorColor = Color.White
                            )
                        )
                    }
                    Button(
                        onClick = { alterarCarroViewModel.salvarAlteracoes() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFAA162C),
                            contentColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Salvar Alterações") }
                }
            }
        }
    }
}