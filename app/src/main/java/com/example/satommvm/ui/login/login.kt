package com.example.satommvm.ui.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation

import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.satommvm.R

@Composable
fun Login(
    navController: NavHostController,
    loginViewModel: LoginViewModel
) {
    val uiState by loginViewModel.uiState.collectAsState()
    val context = LocalContext.current




    LaunchedEffect(key1 = uiState) {
        // Se o login deu certo, navega
        uiState.loginSuccess?.let { user ->
            navController.navigate("dashboard/${user.nome}") {
                popUpTo("login") { inclusive = true }
            }
            loginViewModel.resetLoginState()
        }

        // Se deu erro, mostra o Toast
        uiState.errorMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            loginViewModel.resetLoginState()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            color = Color.Black,
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.sato),
                    contentDescription = "Logo",
                    modifier = Modifier.height(60.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = uiState.username,
                    onValueChange = { loginViewModel.updateUsername(it) },
                    label = { Text("Utilizador") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))


                OutlinedTextField(
                    value = uiState.password,
                    onValueChange = { loginViewModel.updatePassword(it) },
                    label = { Text("Senha") },
                    modifier = Modifier.fillMaxWidth(),


                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),


                )


                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { loginViewModel.login() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAA162C), contentColor = Color.White),
                    enabled = !uiState.isLoading
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.height(24.dp))
                    } else {
                        Text("Entrar")
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { navController.navigate("cadastroUsuario") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAA162C), contentColor = Color.White)
                ) {
                    Text("Registar")
                }
            }
        }
    }
}