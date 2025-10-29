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



@Composable
fun CadastroUsuario(
    navController: NavHostController,
    cadastroUsuarioViewModel: CadastroUsuarioViewModel
) {

    val uiState by cadastroUsuarioViewModel.uiState.collectAsState()
    val context = LocalContext.current


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

        OutlinedTextField(
            value = uiState.username,

            onValueChange = { cadastroUsuarioViewModel.updateUsername(it) },
            label = { Text("Nome") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))


        OutlinedTextField(
            value = uiState.password,

            onValueChange = { cadastroUsuarioViewModel.updatePassword(it) },
            label = { Text("Senha") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(24.dp))


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