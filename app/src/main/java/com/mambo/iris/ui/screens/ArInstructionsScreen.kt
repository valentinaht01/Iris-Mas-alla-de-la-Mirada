package com.mambo.iris.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mambo.iris.ui.theme.IrisFondoGris
import com.mambo.iris.vr.ArExperienceActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArInstructionsScreen(navController: NavController) {

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ver en realidad aumentada") },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Apunta la cámara al zootropo para ver la obra en realidad aumentada.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    // Si quieres pasar el nombre del modelo, puedes usar un extra:
                    val intent = Intent(context, ArExperienceActivity::class.java).apply {
                        putExtra("MODEL_NAME", "zootropo.glb")
                    }
                    context.startActivity(intent)
                }
            ) {
                Text("Iniciar experiencia AR")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { navController.popBackStack() }
            ) {
                Text("Volver")
            }
        }
    }
}






