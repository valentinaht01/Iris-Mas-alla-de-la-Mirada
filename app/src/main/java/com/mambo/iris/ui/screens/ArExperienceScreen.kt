package com.mambo.iris.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.mambo.iris.R
import com.mambo.iris.ui.theme.NaranjaIris
import com.mambo.iris.util.BitmapLoader
import com.mambo.iris.vr.EquirectangularGLView

/**
 * Pantalla de experiencia 360°:
 * - Carga el bitmap con downsampling.
 * - Maneja errores sin crashear la app.
 * - Muestra el visor 360° usando EquirectangularGLView.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArExperienceScreen(
    navController: NavController,
    panoramaResId: Int = R.drawable.logo_fondo // luego lo cambias por tu equirectangular real
) {
    val ctx = LocalContext.current

    // Estados de la UI
    val isLoading = remember { mutableStateOf(true) }
    val errorMessage = remember { mutableStateOf<String?>(null) }
    val bitmapState = remember { mutableStateOf<Bitmap?>(null) }

    // Carga del bitmap en segundo plano
    LaunchedEffect(panoramaResId) {
        try {
            isLoading.value = true
            errorMessage.value = null

            val bmp = BitmapLoader.decodeSampledBitmapFromResource(
                res = ctx.resources,
                resId = panoramaResId,
                reqWidth = 4096,   // tamaño razonable para 360°
                reqHeight = 2048
            )

            if (bmp == null) {
                errorMessage.value = "No se pudo decodificar la imagen 360°."
            } else {
                bitmapState.value = bmp
            }
        } catch (e: Exception) {
            errorMessage.value = e.message ?: "Error desconocido al cargar la imagen 360°."
        } finally {
            isLoading.value = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Experiencia 360°") }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            // 1) Cargando
            if (isLoading.value) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
                return@Box
            }

            // 2) Error
            val err = errorMessage.value
            if (err != null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = err,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        Button(
                            onClick = { navController.popBackStack() },
                            colors = ButtonDefaults.buttonColors(containerColor = NaranjaIris)
                        ) {
                            Text("Volver")
                        }
                        Spacer(modifier = Modifier.padding(4.dp))
                    }
                }
                return@Box
            }

            // 3) Bitmap OK → mostramos el visor 360
            val bitmap = bitmapState.value
            if (bitmap != null) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { context ->
                        EquirectangularGLView(context, bitmap)
                    }
                )
            } else {
                Text(
                    text = "No hay imagen para mostrar.",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

