package com.example.satommvm.ui.view



import android.content.Context
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
import com.example.satommvm.data.dao.CarroDao
import com.example.satommvm.data.model.Carro
import com.example.satommvm.ui.viewmodel.CarroViewModel
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CadastroCarro(navController: NavHostController, carroViewModel: CarroViewModel) {
    var nome by remember { mutableStateOf("") }
    var modelo by remember { mutableStateOf("") }
    var ano by remember { mutableStateOf("") }
    var placa by remember { mutableStateOf("") }
    var imagemUri by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { imagemUri = it.toString() }
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
            ) {
                Text("Selecionar Imagem")
            }
            imagemUri?.let { uri ->
                Spacer(modifier = Modifier.height(16.dp))
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = "Imagem selecionada",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = modelo,
                onValueChange = { modelo = it },
                label = { Text("Modelo") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = ano,
                onValueChange = { ano = it },
                label = { Text("Ano") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = placa,
                onValueChange = { placa = it },
                label = { Text("Placa") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (nome.isNotEmpty() && modelo.isNotEmpty() && ano.isNotEmpty() && placa.isNotEmpty() && imagemUri != null) {
                        val caminhoImagem = saveImageToInternalStorage(
                            context,
                            Uri.parse(imagemUri),
                            "carro_${placa}.jpg"
                        )

                        val novoCarro = Carro(
                            nome = nome,
                            modelo = modelo,
                            ano = ano.toIntOrNull() ?: 0,
                            placa = placa,
                            imagemUri = caminhoImagem
                        )

                        carroViewModel.addCarro(novoCarro) {
                            Toast.makeText(context, "Carro salvo com sucesso!", Toast.LENGTH_SHORT).show()
                            nome = ""
                            modelo = ""
                            ano = ""
                            placa = ""
                            imagemUri = null
                            navController.popBackStack()
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Preencha todos os campos e adicione uma imagem!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFAA162C),
                    contentColor = Color.White
                )
            ) {
                Text("Salvar Carro")
            }
        }
    }
}

fun saveImageToInternalStorage(context: Context, uri: Uri, fileName: String): String {
    val inputStream = context.contentResolver.openInputStream(uri)
    val file = File(context.filesDir, fileName)
    inputStream.use { input ->
        file.outputStream().use { output ->
            input?.copyTo(output)
        }
    }
    return file.absolutePath
}
