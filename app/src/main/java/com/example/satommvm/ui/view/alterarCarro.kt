package com.example.satommvm.ui.view

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
import com.example.satommvm.data.model.Carro
import com.example.satommvm.ui.viewmodel.CarroViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlterarCarro(
    navController: NavHostController,
    placa: String,
    carroViewModel: CarroViewModel
) {
    val lista by carroViewModel.carros.collectAsState(initial = emptyList())
    val carro = lista.find { it.placa == placa }

    var nome by remember(carro) { mutableStateOf(carro?.nome.orEmpty()) }
    var modelo by remember(carro) { mutableStateOf(carro?.modelo.orEmpty()) }
    var ano by remember(carro) { mutableStateOf(carro?.ano?.toString().orEmpty()) }
    var imagemUri by remember(carro) { mutableStateOf(carro?.imagemUri) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { imagemUri = it.toString() }
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
                    ) {
                        Text("Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        }
    ) { padding ->
        carro?.let {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // Imagem do carro
                imagemUri?.let {
                    Image(
                        painter = rememberAsyncImagePainter(it),
                        contentDescription = "Imagem do carro",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clickable { launcher.launch("image/*") },
                        contentScale = ContentScale.Crop
                    )
                }

                Button(
                    onClick = { launcher.launch("image/*") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFAA162C),
                        contentColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Selecionar/Alterar Imagem") }

                // Campos de texto dentro de Card escuro
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
                ) {
                    TextField(
                        value = nome,
                        onValueChange = { nome = it },
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
                        value = modelo,
                        onValueChange = { modelo = it },
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
                        value = ano,
                        onValueChange = { ano = it },
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
                    onClick = {
                        scope.launch {
                            val carroAtualizado = carro.copy(
                                nome = nome,
                                modelo = modelo,
                                ano = ano.toIntOrNull() ?: carro.ano,
                                imagemUri = imagemUri
                            )
                            carroViewModel.updateCarro(carroAtualizado)
                            Toast.makeText(context, "Carro atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFAA162C),
                        contentColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Salvar Alterações") }
            }
        } ?: Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Carro não encontrado ou ainda carregando...", color = Color.White)
        }
    }
}
