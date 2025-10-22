import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.satommvm.ui.viewmodel.CarroViewModel
import com.example.satommvm.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dashboard(
    nome: String,
    navController: NavHostController,
    carroViewModel: CarroViewModel
) {
    val lista by carroViewModel.carros.collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope() // 🔹 coroutine scope para deletes

    Scaffold(
        containerColor = Color.Black,
        topBar = {
            TopAppBar(title = { Text("Gestão de Carros - Bem-vindo, $nome!") })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("cadastroCarro") },
                containerColor = Color(0xFFAA162C),
                contentColor = Color.White
            ) { Text("+") }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (lista.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Nenhum carro cadastrado.", color = Color.White)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(lista) { carro ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { navController.navigate("alterarCarro/${carro.placa}") }, // 🔹 rota corrigida
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("Nome: ${carro.nome}", style = MaterialTheme.typography.titleMedium)
                                    Text("Modelo: ${carro.modelo}")
                                    Text("Ano: ${carro.ano}")
                                    Text("Placa: ${carro.placa}")
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Image(
                                    painter = carro.imagemUri?.let { rememberAsyncImagePainter(it) }
                                        ?: carro.imagemRes?.let { painterResource(id = it) }
                                        ?: painterResource(id = R.drawable.hb20),
                                    contentDescription = carro.nome,
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clickable { navController.navigate("alterarCarro/${carro.placa}") },
                                    contentScale = ContentScale.Crop
                                )
                            }

                            Button(
                                onClick = {
                                    scope.launch {
                                        carroViewModel.deleteCarro(carro) // 🔹 agora dentro da coroutine
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFAA162C),
                                    contentColor = Color.White
                                )
                            ) { Text("Deletar") }
                        }
                    }
                }
            }
        }
    }
}
