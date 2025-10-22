package com.example.satommvm.ui.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.satommvm.data.database.AppDatabase
import com.example.satommvm.data.repository.UsuarioRepository
import com.example.satommvm.ui.viewmodel.CarroViewModel
import com.example.satommvm.ui.viewmodel.UsuarioViewModel
import Dashboard
import Login
import androidx.compose.ui.Modifier

@Composable
fun AppNavigation(context: Context, modifier: Modifier) {
    val navController = rememberNavController()
    val db = remember { AppDatabase.getDatabase(context) }

    // DAOs
    val carroDao = db.carroDao()
    val usuarioDao = db.usuarioDao()

    // Repositories
    val usuarioRepository = UsuarioRepository(usuarioDao)

    // ViewModels
    val usuarioViewModel = remember { UsuarioViewModel(usuarioRepository) }
    val carroViewModel: CarroViewModel = remember { CarroViewModel(carroDao) }

    // Observa lista de carros (se necessário)
    val listaCarros by carroDao.getCarros().collectAsState(initial = emptyList())

    NavHost(navController = navController, startDestination = "login") {

        // Tela de Login
        composable("login") {
            Login(navController = navController, viewModel = usuarioViewModel)
        }

        // Dashboard (Tela2) com parâmetro "nome"
        composable(
            route = "dasboard/{nome}",
            arguments = listOf(navArgument("nome") {
                type = NavType.StringType
                defaultValue = "sem nome"
            })
        ) { backStackEntry ->
            val nome = backStackEntry.arguments?.getString("nome") ?: "sem nome"
            Dashboard(
                nome = nome,
                navController = navController,
                carroViewModel = carroViewModel // agora passando o ViewModel real
            )
        }

        /*
        // Tela de Cadastro de Carro (Tela3)
        composable("tela3") {
            Tela3(navController = navController, carroViewModel = carroViewModel)
        }

        // Tela de Alterar Carro (Tela4)
        composable("tela4/{placa}") { backStackEntry ->
            val placa = backStackEntry.arguments?.getString("placa")
            val carro = listaCarros.find { it.placa == placa }
            if (carro != null) {
                Tela4(navController = navController, carro = carro, carroViewModel = carroViewModel)
            } else {
                navController.popBackStack()
            }
        }
        */
    }
}
