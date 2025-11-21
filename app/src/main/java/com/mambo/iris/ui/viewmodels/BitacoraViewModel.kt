package com.mambo.iris.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class BitacoraUiState(
    val note: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val saveSuccess: Boolean = false
)

class BitacoraViewModel : ViewModel() {
    private val auth = Firebase.auth
    private val firestore = Firebase.firestore
    private val userId: String?
        get() = auth.currentUser?.uid

    private val _uiState = MutableStateFlow(BitacoraUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadNote()
    }

    fun loadNote() {
        val currentUserId = userId ?: run {
            _uiState.update { it.copy(error = "Usuario no autenticado.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, saveSuccess = false) }
            try {
                val document = firestore
                    .collection("users")
                    .document(currentUserId)
                    .get()
                    .await()

                val savedNote = document.getString("bitacora_note") ?: ""
                _uiState.update { it.copy(note = savedNote, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = e.message ?: "No se pudo cargar la bitácora.",
                        isLoading = false
                    )
                }
            }
        }
    }

    fun saveNote(newNote: String) {
        val currentUserId = userId ?: run {
            _uiState.update { it.copy(error = "Usuario no autenticado.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, saveSuccess = false) }
            try {
                val noteData = hashMapOf("bitacora_note" to newNote)

                firestore
                    .collection("users")
                    .document(currentUserId)
                    .set(noteData, SetOptions.merge())
                    .await()

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        saveSuccess = true,
                        note = newNote
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = e.message ?: "No se pudo guardar la bitácora.",
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onNoteChange(newNote: String) {
        _uiState.update { it.copy(note = newNote, saveSuccess = false, error = null) }
    }

    fun resetSaveStatus() {
        _uiState.update { it.copy(saveSuccess = false, error = null) }
    }
}
