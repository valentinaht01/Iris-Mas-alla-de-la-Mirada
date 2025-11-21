package com.mambo.iris.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mambo.iris.R
import com.mambo.iris.auth.AuthUiState
import com.mambo.iris.auth.AuthViewModel
import com.mambo.iris.ui.navigation.AppRoute
import com.mambo.iris.ui.theme.IrisAppTheme
import com.mambo.iris.ui.theme.IrisCardGris
import com.mambo.iris.ui.theme.IrisFondoGris
import com.mambo.iris.ui.theme.NaranjaIris

@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    val authState by authViewModel.authUiState.collectAsState()
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // --- Effects for handling UI state ---
    LaunchedEffect(authState) {
        when (val state = authState) {
            is AuthUiState.LoginSuccess -> {
                navController.navigate(AppRoute.Home.route) {
                    popUpTo(AppRoute.Login.route) { inclusive = true }
                }
            }
            is AuthUiState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                authViewModel.resetAuthState() // Reset state after showing error
            }
            else -> Unit // Idle, Loading, RegistrationSuccess
        }
    }

    // Reset state when the screen is disposed
    DisposableEffect(Unit) {
        onDispose {
            authViewModel.resetAuthState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(IrisFondoGris),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_login_register),
                contentDescription = "Logo de Iris",
                modifier = Modifier.size(150.dp)
            )
        }

        // Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
            colors = CardDefaults.cardColors(containerColor = IrisCardGris)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "Iniciar Sesión",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    colors = authTextFieldColors()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    colors = authTextFieldColors()
                )

                Spacer(modifier = Modifier.height(8.dp))

                ClickableText(
                    text = AnnotatedString("¿Has olvidado tu contraseña?"),
                    onClick = {
                        if (email.isNotBlank()) {
                            authViewModel.sendPasswordReset(email) { result ->
                                val message = if (result.isSuccess) {
                                    "Se ha enviado un correo para restablecer tu contraseña."
                                } else {
                                    "Error al enviar el correo. Verifica la dirección."
                                }
                                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                            }
                        } else {
                            Toast.makeText(context, "Por favor, ingresa tu correo electrónico.", Toast.LENGTH_SHORT).show()
                        }
                    },
                    style = TextStyle(
                        color = Color.White.copy(alpha = 0.7f),
                        textAlign = TextAlign.End
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        authViewModel.signInWithEmail(
                            email.trim(),
                            password.trim()
                        )
                    },
                    enabled = authState !is AuthUiState.Loading,
                    modifier = Modifier.height(50.dp),
                    contentPadding = PaddingValues(horizontal = 32.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = NaranjaIris)
                ) {
                    if (authState is AuthUiState.Loading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    } else {
                        Text(
                            text = "Iniciar sesión",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(onClick = {
                    navController.navigate(AppRoute.SignUp.route) {
                        launchSingleTop = true
                    }
                }) {
                    Text("¿No tienes cuenta? Crea una", color = Color.White)
                }
            }
        }
    }
}

@Composable
private fun authTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Color.Transparent,
    unfocusedBorderColor = Color.Transparent,
    cursorColor = Color.Black,
    focusedTextColor = Color.Black,
    unfocusedTextColor = Color.Black,
    focusedLabelColor = Color.Gray,
    unfocusedLabelColor = Color.Gray,
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White
)

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    IrisAppTheme {
        LoginScreen(navController = rememberNavController())
    }
}
