package com.mambo.iris.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class UserProfile(
    val nombre: String = "",
    val apellido: String = "",
    val email: String = "",
    val movimientoFavorito: String = "",
    val biografia: String = "",
    val fotoPerfil: String = "",
    val bitacoraNote: String = ""
)

data class ProfileUiState(
    val profile: UserProfile? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val updateSuccess: Boolean = false
)

class ProfileViewModel : ViewModel() {
    private val auth = Firebase.auth
    private val firestore = Firebase.firestore
    private val storage = Firebase.storage
    private val userId: String?
        get() = auth.currentUser?.uid

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    fun loadUserProfile() { /* ... existing code ... */ }

    fun updateUserProfile(newName: String, newMovement: String, newBio: String) { /* ... existing code ... */ }

    fun uploadAvatar(uri: Uri) {
        val currentUserId = userId ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val storageRef = storage.reference.child("avatars/$currentUserId.jpg")
                storageRef.putFile(uri).await()
                val downloadUrl = storageRef.downloadUrl.await().toString()

                firestore.collection("users").document(currentUserId).update("fotoPerfil", downloadUrl).await()
                loadUserProfile() // Reload to get the new avatar URL
                _uiState.update { it.copy(isLoading = false, updateSuccess = true) }

            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun resetUpdateStatus() {
        _uiState.update { it.copy(updateSuccess = false, error = null) }
    }
}