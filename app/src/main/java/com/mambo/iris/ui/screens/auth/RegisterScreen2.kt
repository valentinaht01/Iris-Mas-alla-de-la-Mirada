package com.mambo.iris.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mambo.iris.auth.AuthUiState
import com.mambo.iris.auth.AuthViewModel
import com.mambo.iris.ui.navigation.AppRoute

@Composable
fun RegisterScreen2(
    nav: NavController,
    vm: AuthViewModel = viewModel(),
    email: String,
    password: String
) {
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var movimiento by remember { mutableStateOf("") }
    var biografia by remember { mutableStateOf("") }

    val authState by vm.authUiState.collectAsState()
    val context = LocalContext.current

    // --- Effects for handling UI state ---
    LaunchedEffect(authState) {
        when (val state = authState) {
            is AuthUiState.RegistrationSuccess -> {
                nav.navigate(AppRoute.Home.route) {
                    // Pop everything up to the start of the graph to prevent going back to auth flow
                    popUpTo(nav.graph.startDestinationId) { inclusive = true }
                }
            }
            is AuthUiState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                vm.resetAuthState() // Reset state after showing error
            }
            else -> Unit // Idle, Loading, LoginSuccess
        }
    }

    // Reset state when the screen is disposed
    DisposableEffect(Unit) {
        onDispose {
            vm.resetAuthState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Completa tu perfil", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = apellido,
            onValueChange = { apellido = it },
            label = { Text("Apellido") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = movimiento,
            onValueChange = { movimiento = it },
            label = { Text("Movimiento artístico favorito") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = biografia,
            onValueChange = { biografia = it },
            label = { Text("Biografía breve") },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 100.dp)
        )
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                val n = nombre.trim()
                val a = apellido.trim()
                val m = movimiento.trim()
                val b = biografia.trim()

                if (n.isBlank() || a.isBlank()) { // Only name and lastname are mandatory for example
                    Toast.makeText(context, "Nombre y apellido son obligatorios", Toast.LENGTH_SHORT).show()
                } else {
                    vm.registrarUsuario(
                        email = email.trim(),
                        contrasena = password.trim(),
                        nombre = n,
                        apellido = a,
                        movimientoFavorito = m,
                        biografia = b
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = authState !is AuthUiState.Loading
        ) {
            if (authState is AuthUiState.Loading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                Text("Crear cuenta")
            }
        }
    }
}
