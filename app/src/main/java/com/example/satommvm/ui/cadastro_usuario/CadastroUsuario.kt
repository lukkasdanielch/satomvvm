package com.example.satommvm.ui.cadastro_usuario

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
// Importa o ViewModel do mesmo pacote
import com.example.satommvm.ui.cadastro_usuario.CadastroUsuarioViewModel

@Composable
fun CadastroUsuario(
    navController: NavHostController,
    cadastroUsuarioViewModel: CadastroUsuarioViewModel
) {
    // A View lê o estado do ViewModel
    val uiState by cadastroUsuarioViewModel.uiState.collectAsState()
    val context = LocalContext.current

    // A View reage a mudanças de estado (sucesso ou erro)
    LaunchedEffect(key1 = uiState) {
        if (uiState.registerSuccess) {
            Toast.makeText(context, "Cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
            cadastroUsuarioViewModel.resetRegisterState()
            navController.popBackStack() // volta para a tela de login
        }
        uiState.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            cadastroUsuarioViewModel.resetRegisterState()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Cadastro de Usuário",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // O TextField lê o 'uiState.username'
        OutlinedTextField(
            value = uiState.username,
            // A View notifica o ViewModel da mudança
            onValueChange = { cadastroUsuarioViewModel.updateUsername(it) },
            label = { Text("Nome") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // O TextField lê o 'uiState.password'
        OutlinedTextField(
            value = uiState.password,
            // A View notifica o ViewModel da mudança
            onValueChange = { cadastroUsuarioViewModel.updatePassword(it) },
            label = { Text("Senha") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // O Botão notifica o ViewModel que o utilizador quer cadastrar
        Button(
            onClick = { cadastroUsuarioViewModel.cadastrar() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            enabled = !uiState.isLoading
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text("Cadastrar")
            }
        }
    }
}