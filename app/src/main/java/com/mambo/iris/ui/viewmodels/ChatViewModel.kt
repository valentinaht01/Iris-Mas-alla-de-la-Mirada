// com/mambo/iris/ui/viewmodels/ChatViewModel.kt
package com.mambo.iris.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.mambo.iris.BuildConfig
import com.mambo.iris.data.repository.MuseumRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ChatMessage(
    val text: String,
    val isUser: Boolean
)

class ChatViewModel(
    private val repo: MuseumRepository
) : ViewModel() {

    private val model = GenerativeModel(
        modelName = "gemini-1.5-flash-latest",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages

    fun sendMessage(userMessage: String) {
        viewModelScope.launch {
            // pinta el mensaje del usuario
            _messages.value = _messages.value + ChatMessage(userMessage, true)

            try {
                // contexto desde firestore
                val context = repo.buildMuseumContextText()

                val prompt = """
                    Eres el asistente del Museo Virtual Iris.
                    Usa este contexto para responder:

                    $context

                    Pregunta del usuario: "$userMessage"
                """.trimIndent()

                val response = model.generateContent(prompt)
                val reply = response.text ?: "No pude generar una respuesta."

                _messages.value = _messages.value + ChatMessage(reply, false)
            } catch (e: Exception) {
                _messages.value =
                    _messages.value + ChatMessage("Error: ${e.message}", false)
            }
        }
    }
}



