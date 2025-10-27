package com.example.satommvm.ui.cadastro_carro

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
fun CadastroCarro(
    navController: NavHostController,
    cadastroCarroViewModel: CadastroCarroViewModel // <-- CORREÇÃO
) {
    val uiState by cadastroCarroViewModel.uiState.collectAsState()
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        cadastroCarroViewModel.updateCadastroImagemUri(uri?.toString())
    }

    LaunchedEffect(key1 = uiState) {
        if (uiState.isSaveSuccess) {
            Toast.makeText(context, "Carro salvo com sucesso!", Toast.LENGTH_SHORT).show()
            cadastroCarroViewModel.resetCadastroCarroState()
            navController.popBackStack()
        }
        uiState.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            cadastroCarroViewModel.resetCadastroCarroState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cadastro de Carro", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFAA162C))
            )
        },
        containerColor = Color.Black
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { launcher.launch("image/*") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFAA162C),
                    contentColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            ) { Text("Selecionar Imagem") }

            uiState.imagemUri?.let { uri ->
                Spacer(modifier = Modifier.height(16.dp))
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = "Imagem selecionada",
                    modifier = Modifier.fillMaxWidth().height(180.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.nome,
                onValueChange = { cadastroCarroViewModel.updateCadastroNome(it) },
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = uiState.modelo,
                onValueChange = { cadastroCarroViewModel.updateCadastroModelo(it) },
                label = { Text("Modelo") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = uiState.ano,
                onValueChange = { cadastroCarroViewModel.updateCadastroAno(it) },
                label = { Text("Ano") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = uiState.placa,
                onValueChange = { cadastroCarroViewModel.updateCadastroPlaca(it) },
                label = { Text("Placa") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    cadastroCarroViewModel.salvarCarro(context.applicationContext)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFAA162C),
                    contentColor = Color.White
                ),
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text("Salvar Carro")
                }
            }
        }
    }
}