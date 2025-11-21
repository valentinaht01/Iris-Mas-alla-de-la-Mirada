package com.mambo.iris.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mambo.iris.ui.theme.IrisFondoGris
import com.mambo.iris.ui.theme.NaranjaIris
import com.mambo.iris.ui.theme.workSansRegular
import com.mambo.iris.ui.viewmodels.BitacoraViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BitacoraScreen(
    vm: BitacoraViewModel = viewModel()
) {
    val uiState by vm.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            Toast.makeText(context, "¡Bitácora guardada!", Toast.LENGTH_SHORT).show()
            vm.resetSaveStatus()
        }
    }
    
    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            Toast.makeText(context, "Error: $it", Toast.LENGTH_LONG).show()
            vm.resetSaveStatus()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Bitácora") },
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
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = uiState.note,
                onValueChange = { vm.onNoteChange(it) },
                label = { Text("Escribe tus experiencias y reflexiones...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NaranjaIris,
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = NaranjaIris,
                    focusedLabelColor = NaranjaIris,
                    unfocusedLabelColor = Color.Gray,
                    focusedContainerColor = Color.DarkGray.copy(alpha = 0.2f),
                    unfocusedContainerColor = Color.DarkGray.copy(alpha = 0.2f)
                ),
                textStyle = TextStyle(fontFamily = workSansRegular, fontSize = 16.sp, color = Color.White)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { vm.saveNote(uiState.note) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !uiState.isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = NaranjaIris)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Guardar Bitácora", color = Color.White)
                }
            }
        }
    }
}
