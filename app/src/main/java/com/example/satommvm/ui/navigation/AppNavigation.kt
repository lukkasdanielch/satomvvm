package com.example.satommvm.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument


import com.example.satommvm.ui.login.Login
import com.example.satommvm.ui.cadastro_usuario.CadastroUsuario
import com.example.satommvm.ui.dashboard.Dashboard
import com.example.satommvm.ui.cadastro_carro.CadastroCarro
import com.example.satommvm.ui.alterar_carro.AlterarCarro

import com.example.satommvm.ui.login.LoginViewModel
import com.example.satommvm.ui.cadastro_usuario.CadastroUsuarioViewModel
import com.example.satommvm.ui.dashboard.DashboardViewModel
import com.example.satommvm.ui.cadastro_carro.CadastroCarroViewModel
import com.example.satommvm.ui.alterar_carro.AlterarCarroViewModel


import com.example.satommvm.ui.foto_carro.FotoCarro
import com.example.satommvm.ui.foto_carro.FotoCarroViewModel

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel,
    cadastroUsuarioViewModel: CadastroUsuarioViewModel,
    dashboardViewModel: DashboardViewModel,
    cadastroCarroViewModel: CadastroCarroViewModel,
    alterarCarroViewModel: AlterarCarroViewModel,
    fotoCarroViewModel: FotoCarroViewModel
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login", modifier = modifier) {

        // ... (Rotas de Login, CadastroUsuario, Dashboard, CadastroCarro, AlterarCarro) ...
        composable("login") { Login(navController, loginViewModel) }
        composable("cadastroUsuario") { CadastroUsuario(navController, cadastroUsuarioViewModel) }
        composable("dashboard/{nome}") { b -> Dashboard(b.arguments?.getString("nome")?:"", navController, dashboardViewModel) }
        composable("cadastroCarro") { CadastroCarro(navController, cadastroCarroViewModel) }
        composable("alterarCarro/{placa}") { b -> AlterarCarro(navController, b.arguments?.getString("placa")?:"", alterarCarroViewModel) }


        composable(
            "fotoCarro/{carroId}",
            arguments = listOf(navArgument("carroId") { type = NavType.IntType })
        ) { backStackEntry ->
            val carroId = backStackEntry.arguments?.getInt("carroId") ?: 0
            FotoCarro(
                navController = navController,
                fotoCarroViewModel = fotoCarroViewModel,
                carroId = carroId
            )
        }
    }
}