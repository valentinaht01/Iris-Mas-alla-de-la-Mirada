package com.mambo.iris.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mambo.iris.data.model.Artwork
import com.mambo.iris.data.repository.MuseumRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GalleryViewModel(
    private val repository: MuseumRepository
) : ViewModel() {

    private val _artworks = MutableStateFlow<List<Artwork>>(emptyList())
    val artworks: StateFlow<List<Artwork>> = _artworks

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loadArtworks()
    }

    private fun loadArtworks() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val allArtworks = repository.getAllArtworks()
                _artworks.value = allArtworks
            } catch (e: Exception) {
                _error.value = "Error al cargar las obras: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}




