package com.mambo.iris.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.mambo.iris.ui.navigation.AppRoute
import com.mambo.iris.ui.theme.IrisFondoGris
import com.mambo.iris.vr.ArExperienceActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController) {

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Colección de Obras") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = IrisFondoGris,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = IrisFondoGris
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // 1) Secuencia de la imagen – Sair García
            ArtworkCard(
                artworkTitle = "Secuencia de la imagen",
                artworkSubtitle = "Montage Sair García",
                artworkImagePath = "images/obra_secuencias.jpg",
                artworkDescription = "Descripción de la obra de Sair García...",
                artistDescription = "Biografía de Sair García...",
                hasAr = true,
                onViewAr = { navController.navigate(AppRoute.ArInstructions.route) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 2) Inflaleve – Oscar Muñoz (modelo nflaleveMemoria.glb)
            ArtworkCard(
                artworkTitle = "Inflaleve, memoria y fragilidad",
                artworkSubtitle = "La obra de Oscar Muñoz",
                artworkImagePath = "images/obra_inflaleve.jpg",
                artworkDescription = "Descripción de la obra de Oscar Muñoz...",
                artistDescription = "Biografía de Oscar Muñoz...",
                hasAr = true,
                onViewAr = {
                    val intent = Intent(context, ArExperienceActivity::class.java).apply {
                        putExtra(
                            ArExperienceActivity.EXTRA_MODEL_PATH,
                            "models/nflaleveMemoria.glb"
                        )
                        putExtra(
                            ArExperienceActivity.EXTRA_TITLE,
                            "Inflaleve, memoria y fragilidad"
                        )
                    }
                    context.startActivity(intent)
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 3) El Rostro de Cristo – Edgar Negret (NUEVA OBRA)
            ArtworkCard(
                artworkTitle = "El Rostro de Cristo",
                artworkSubtitle = "La obra de Edgar Negret",
                artworkImagePath = "images/elRostroDeCristo.jpg",
                artworkDescription = "Descripción de la obra de Edgar Negret...",
                artistDescription = "Biografía de Edgar Negret...",
                hasAr = true,
                onViewAr = {
                    val intent = Intent(context, ArExperienceActivity::class.java).apply {
                        putExtra(
                            ArExperienceActivity.EXTRA_MODEL_PATH,
                            "models/elRostroDeCristo.glb"
                        )
                        putExtra(
                            ArExperienceActivity.EXTRA_TITLE,
                            "El Rostro de Cristo"
                        )
                    }
                    context.startActivity(intent)
                }
            )
        }
    }
}

@Composable
private fun ArtworkCard(
    artworkTitle: String,
    artworkSubtitle: String,
    artworkImagePath: String,
    artworkDescription: String,
    artistDescription: String,
    hasAr: Boolean,
    onViewAr: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = artworkTitle,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = artworkSubtitle,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(16.dp))

            AsyncImage(
                model = "file:///android_asset/$artworkImagePath",
                contentDescription = artworkTitle,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = artworkDescription,
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onViewAr,
                enabled = hasAr,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (hasAr) Color.White else Color.LightGray,
                    contentColor = if (hasAr) Color.Black else Color.DarkGray
                )
            ) {
                Text("Ver en RA")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = artistDescription,
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp
            )
        }
    }
}

