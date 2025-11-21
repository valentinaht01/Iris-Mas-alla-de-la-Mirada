// com/mambo/iris/data/repository/MuseumRepository.kt
package com.mambo.iris.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.mambo.iris.data.model.Artwork
import kotlinx.coroutines.tasks.await

class MuseumRepository {

  val auth: FirebaseAuth
    get() = Firebase.auth

  val firestore: FirebaseFirestore
    get() = Firebase.firestore

  fun hasUser(): Boolean = auth.currentUser != null

  fun getCurrentUser() = auth.currentUser

  fun signOut() = auth.signOut()

  /**
   * Obtiene todas las obras de Firestore (colección "artworks")
   */
  suspend fun getAllArtworks(): List<Artwork> {
    return firestore.collection("artworks")
      .get()
      .await()
      .toObjects()
  }

  /**
   * Texto que usará el chat como contexto.
   */
  suspend fun buildMuseumContextText(): String {
    val works = getAllArtworks()
    if (works.isEmpty()) {
      return "No hay obras registradas en el museo."
    }

    return works.joinToString(separator = "\n\n") { w ->
      """
            Obra: ${w.displayTitle()}
            Artista: ${w.displayArtist()}
            Año: ${w.year ?: "N/A"}
            Sala: ${w.roomId ?: "N/A"}
            Descripción: ${w.description ?: "Sin descripción"}
            """.trimIndent()
    }
  }
}





