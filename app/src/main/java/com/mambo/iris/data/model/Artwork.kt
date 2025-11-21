// com/mambo/iris/data/model/Artwork.kt
package com.mambo.iris.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Artwork(
    val id: String? = null,

    // en Firestore / json puede venir como "title" o como "name"
    val title: String? = null,
    val name: String? = null,

    // puede venir como "artist" o como "author"
    val artist: String? = null,
    val author: String? = null,

    // a veces lo guardan como string
    val year: String? = null,

    val description: String? = null,

    // tú en assets lo tenías como imageFile, en json como imageAsset
    val imageFile: String? = null,
    val imageAsset: String? = null,

    val roomId: String? = null,
    val tags: List<String> = emptyList()
 )
  {
    // helpers para el UI
    fun displayTitle(): String = title ?: name ?: "Sin título"
    fun displayArtist(): String = artist ?: author ?: "Autor desconocido"
    fun displayImage(): String? = imageFile ?: imageAsset
   }



