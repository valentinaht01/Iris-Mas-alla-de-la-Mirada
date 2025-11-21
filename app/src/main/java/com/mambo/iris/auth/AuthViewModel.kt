package com.mambo.iris.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth
    private val firestore = Firebase.firestore

    private val _authUiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val authUiState = _authUiState.asStateFlow()

    fun signInWithEmail(email: String, password: String) {
        viewModelScope.launch {
            _authUiState.value = AuthUiState.Loading
            try {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                _authUiState.value = AuthUiState.LoginSuccess(result.user?.uid ?: "")
            } catch (e: Exception) {
                _authUiState.value = AuthUiState.Error(e.message ?: "Error desconocido al iniciar sesión.")
            }
        }
    }

    fun registrarUsuario(
        email: String,
        contrasena: String,
        nombre: String,
        apellido: String,
        movimientoFavorito: String,
        biografia: String,
        fotoPerfil: String? = null
    ) {
        viewModelScope.launch {
            _authUiState.value = AuthUiState.Loading
            try {
                // 1. Crear cuenta en Firebase Auth
                val result = auth.createUserWithEmailAndPassword(email, contrasena).await()
                val userId = result.user?.uid ?: throw Exception("No se pudo obtener el UID del usuario.")

                // 2. Crear el documento del perfil en Firestore
                val userData = hashMapOf(
                    "nombre" to nombre,
                    "apellido" to apellido,
                    "email" to email,
                    "movimientoFavorito" to movimientoFavorito,
                    "biografia" to biografia,
                    "fotoPerfil" to (fotoPerfil ?: "https://i.postimg.cc/PrHMRqZk/user-default.png"),
                    "intereses" to emptyList<String>()
                )

                firestore.collection("users").document(userId).set(userData).await()

                // 3. Emitir estado de éxito
                _authUiState.value = AuthUiState.RegistrationSuccess

            } catch (e: Exception) {
                // 4. Emitir estado de error
                val errorMessage = when (e) {
                    is FirebaseAuthUserCollisionException -> "El correo electrónico ya está en uso."
                    else -> e.message ?: "Ocurrió un error desconocido."
                }
                _authUiState.value = AuthUiState.Error(errorMessage)
            }
        }
    }

    fun sendPasswordReset(email: String, onComplete: (Result<Unit>) -> Unit) {
        viewModelScope.launch {
            try {
                auth.sendPasswordResetEmail(email).await()
                onComplete(Result.success(Unit))
            } catch (e: Exception) {
                onComplete(Result.failure(e))
            }
        }
    }

    fun resetAuthState() {
        _authUiState.value = AuthUiState.Idle
    }
}

// --- Estados de la UI para Autenticación ---
sealed class AuthUiState {
    object Idle : AuthUiState()                  // Estado inicial
    object Loading : AuthUiState()               // Cargando
    object RegistrationSuccess : AuthUiState()   // Éxito en el registro
    data class LoginSuccess(val uid: String) : AuthUiState() // Éxito en el login
    data class Error(val message: String) : AuthUiState()     // Error
}
