package com.example.satommvm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.satommvm.data.database.AppDatabase
import com.example.satommvm.data.repository.CarroRepository
import com.example.satommvm.data.repository.UsuarioRepository
import com.example.satommvm.ui.navigation.AppNavigation
import com.example.satommvm.ui.viewmodel.CarroViewModel
import com.example.satommvm.ui.viewmodel.UsuarioViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtém instância do banco
        val db = AppDatabase.getDatabase(this)

        // Cria os Repositories
        val carroRepository = CarroRepository(db.carroDao())
        val usuarioRepository = UsuarioRepository(db.usuarioDao())

        // Cria os ViewModels passando os Repositories
        val carroViewModel = CarroViewModel(carroRepository)
        val usuarioViewModel = UsuarioViewModel(usuarioRepository)

        setContent {
            MaterialTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    // Navegação do app
                    AppNavigation(
                        carroDao = db.carroDao(),
                        carroViewModel = carroViewModel,
                        usuarioViewModel = usuarioViewModel
                    )
                }
            }
        }
    }
}
