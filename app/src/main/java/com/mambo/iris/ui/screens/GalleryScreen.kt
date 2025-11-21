package com.mambo.iris.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.mambo.iris.data.model.Artwork
import com.mambo.iris.data.repository.MuseumRepository
import com.mambo.iris.ui.theme.IrisFondoGris
import com.mambo.iris.ui.viewmodels.GalleryViewModel
import com.mambo.iris.ui.viewmodels.GalleryViewModelFactory

@Composable
fun GalleryScreen(
    navController: NavController,
    repo: MuseumRepository
) {
    // AQUÍ usamos el factory nuevo
    val viewModel: GalleryViewModel = viewModel(
        factory = GalleryViewModelFactory(repo)
    )

    val artworks by viewModel.artworks.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(IrisFondoGris)
            .padding(16.dp)
    ) {
        when {
            isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White
                )
            }

            error != null -> {
                Text(
                    text = "Ocurrió un error:\n${error}",
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            }

            artworks.isEmpty() -> {
                Text(
                    text = "No hay obras disponibles",
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            else -> {
                Column {
                    Text(
                        text = "Galería de Obras",
                        color = Color.White,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(artworks) { artwork ->
                            ArtworkCard(
                                artwork = artwork,
                                onClick = {
                                    // aquí luego navegas al detalle
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ArtworkCard(
    artwork: Artwork,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(Color.DarkGray, shape = RoundedCornerShape(8.dp))
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ajusta a tu campo real de Firestore
        val imagePath = "file:///android_asset/images/${artwork.imageFile ?: ""}"

        Image(
            painter = rememberAsyncImagePainter(model = imagePath),
            contentDescription = artwork.title ?: "Obra",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(Color.Gray)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = artwork.title ?: "Sin título",
            color = Color.White,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )

        Text(
            text = artwork.author ?: "",
            color = Color.LightGray,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
    }
}




