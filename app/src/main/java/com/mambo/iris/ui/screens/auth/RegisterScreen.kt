package com.mambo.iris.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mambo.iris.auth.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
  nav: NavController,
  vm: AuthViewModel = viewModel(),
  onNext: (email: String, password: String) -> Unit
) {
  var email by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }
  var errorMessage by remember { mutableStateOf<String?>(null) }
  var isLoading by remember { mutableStateOf(false) }

  val coroutineScope = rememberCoroutineScope()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(32.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text("Crear Nueva Cuenta", style = MaterialTheme.typography.headlineMedium)
    Spacer(Modifier.height(16.dp))

    OutlinedTextField(
      value = email,
      onValueChange = { email = it },
      label = { Text("Correo Electrónico") },
      modifier = Modifier.fillMaxWidth(),
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
      singleLine = true
    )
    Spacer(Modifier.height(8.dp))

    OutlinedTextField(
      value = password,
      onValueChange = { password = it },
      label = { Text("Contraseña") },
      modifier = Modifier.fillMaxWidth(),
      visualTransformation = PasswordVisualTransformation(),
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
      singleLine = true
    )
    Spacer(Modifier.height(16.dp))

    errorMessage?.let {
      Text(text = it, color = MaterialTheme.colorScheme.error)
    }

    Button(
      onClick = {
        if (email.isBlank() || password.isBlank()) {
          errorMessage = "El correo y la contraseña son obligatorios"
          return@Button
        }

        errorMessage = null
        isLoading = true
        coroutineScope.launch {
          onNext(email, password)
          isLoading = false
        }
      },
      modifier = Modifier.fillMaxWidth(),
      enabled = !isLoading
    ) {
      if (isLoading) {
        CircularProgressIndicator(modifier = Modifier.size(24.dp))
      } else {
        Text("Siguiente")
      }
    }
  }
}




