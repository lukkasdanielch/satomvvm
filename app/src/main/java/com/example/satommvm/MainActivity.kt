package com.example.satommvm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.satommvm.data.database.AppDatabase
import com.example.satommvm.data.repository.CarroRepository
import com.example.satommvm.data.repository.FotoCarroRepository
import com.example.satommvm.data.repository.UsuarioRepository
import com.example.satommvm.ui.navigation.AppNavigation

// ... (Imports dos 5 ViewModels)
import com.example.satommvm.ui.login.LoginViewModel
import com.example.satommvm.ui.cadastro_usuario.CadastroUsuarioViewModel
import com.example.satommvm.ui.dashboard.DashboardViewModel
import com.example.satommvm.ui.cadastro_carro.CadastroCarroViewModel
import com.example.satommvm.ui.alterar_carro.AlterarCarroViewModel

// <-- ADICIONAR IMPORT DO NOVO VIEWMODEL
import com.example.satommvm.ui.foto_carro.FotoCarroViewModel


class MainActivity : ComponentActivity() {

    private val db by lazy { AppDatabase.getDatabase(this) }
    private val carroRepository by lazy { CarroRepository(db.carroDao()) }
    private val usuarioRepository by lazy { UsuarioRepository(db.usuarioDao()) }
    private val fotoCarroRepository by lazy { FotoCarroRepository(db.fotoCarroDao()) }

    private val viewModelFactory: ViewModelProvider.Factory by lazy {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return when {
                    // ... (os 5 ViewModels anteriores)
                    modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(usuarioRepository) as T
                    modelClass.isAssignableFrom(CadastroUsuarioViewModel::class.java) -> CadastroUsuarioViewModel(usuarioRepository) as T
                    modelClass.isAssignableFrom(DashboardViewModel::class.java) -> DashboardViewModel(carroRepository) as T
                    modelClass.isAssignableFrom(CadastroCarroViewModel::class.java) -> CadastroCarroViewModel(carroRepository) as T
                    modelClass.isAssignableFrom(AlterarCarroViewModel::class.java) -> AlterarCarroViewModel(carroRepository) as T

                    // <-- ADICIONAR O NOVO VIEWMODEL À FACTORY
                    modelClass.isAssignableFrom(FotoCarroViewModel::class.java) ->
                        FotoCarroViewModel(fotoCarroRepository) as T

                    else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            }
        }
    }

    // Criar os ViewModels
    private val loginViewModel: LoginViewModel by viewModels { viewModelFactory }
    private val cadastroUsuarioViewModel: CadastroUsuarioViewModel by viewModels { viewModelFactory }
    private val dashboardViewModel: DashboardViewModel by viewModels { viewModelFactory }
    private val cadastroCarroViewModel: CadastroCarroViewModel by viewModels { viewModelFactory }
    private val alterarCarroViewModel: AlterarCarroViewModel by viewModels { viewModelFactory }
    private val fotoCarroViewModel: FotoCarroViewModel by viewModels { viewModelFactory } // <-- ADICIONAR

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(color = MaterialTheme.colorScheme.background) {

                    AppNavigation(
                        loginViewModel = loginViewModel,
                        cadastroUsuarioViewModel = cadastroUsuarioViewModel,
                        dashboardViewModel = dashboardViewModel,
                        cadastroCarroViewModel = cadastroCarroViewModel,
                        alterarCarroViewModel = alterarCarroViewModel,
                        fotoCarroViewModel = fotoCarroViewModel
                    )
                }
            }
        }
    }
}