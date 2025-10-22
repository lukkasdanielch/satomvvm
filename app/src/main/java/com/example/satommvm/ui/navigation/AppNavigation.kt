package com.example.satommvm.ui.navigation

import Dashboard
import Login

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.satommvm.data.dao.CarroDao
import com.example.satommvm.ui.view.AlterarCarro
import com.example.satommvm.ui.view.CadastroCarro
import com.example.satommvm.ui.view.CadastroUsuario
import com.example.satommvm.ui.viewmodel.CarroViewModel
import com.example.satommvm.ui.viewmodel.UsuarioViewModel

@Composable
fun AppNavigation(
    carroDao: CarroDao,
    carroViewModel: CarroViewModel,
    usuarioViewModel: UsuarioViewModel,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login", modifier = modifier) {
        // Tela de Login
        composable("login") {
            Login(navController = navController, usuarioViewModel = usuarioViewModel)
        }

        // Tela de Cadastro de Usuário
        composable("cadastroUsuario") {
            CadastroUsuario(navController = navController, usuarioViewModel = usuarioViewModel)
        }

        // Dashboard principal
        composable("dashboard/{nome}") { backStackEntry ->
            val nome = backStackEntry.arguments?.getString("nome") ?: "Usuário"
            Dashboard(
                nome = nome,
                navController = navController,
                carroViewModel = carroViewModel
            )
        }

        // Cadastro de Carros
        composable("cadastroCarro") {
            CadastroCarro(navController = navController, carroViewModel = carroViewModel)
        }

        // Alteração de Carros
        composable("alterarCarro/{placa}") { backStackEntry ->
            val placa = backStackEntry.arguments?.getString("placa") ?: ""
            AlterarCarro(
                navController = navController,
                placa = placa,
                carroViewModel = carroViewModel
            )
        }
    }
}
