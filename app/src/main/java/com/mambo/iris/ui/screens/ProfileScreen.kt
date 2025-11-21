package com.mambo.iris.ui.screens

import android.Manifest
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.mambo.iris.ui.theme.IrisFondoGris
import com.mambo.iris.ui.theme.NaranjaIris
import com.mambo.iris.ui.viewmodels.ProfileViewModel
import com.mambo.iris.ui.viewmodels.UserProfile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(vm: ProfileViewModel = viewModel()) {
    val uiState by vm.uiState.collectAsState()
    val userProfile = uiState.profile
    val context = LocalContext.current

    var showEditDialog by remember { mutableStateOf(false) }

    val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            vm.uploadAvatar(uri)
        } else {
            Toast.makeText(context, "No se seleccionó ninguna imagen", Toast.LENGTH_SHORT).show()
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        } else {
            Toast.makeText(context, "El permiso para acceder a la galería es necesario", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(uiState.updateSuccess) {
        if (uiState.updateSuccess) {
            Toast.makeText(context, "¡Perfil actualizado!", Toast.LENGTH_SHORT).show()
            vm.resetUpdateStatus()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = IrisFondoGris,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = IrisFondoGris
    ) { padding ->
        if (uiState.isLoading && userProfile == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = NaranjaIris)
            }
        } else if (userProfile != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = userProfile.fotoPerfil.ifEmpty { null },
                    contentDescription = "Avatar de usuario",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(Modifier.height(8.dp))

                TextButton(onClick = { 
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                }) {
                    Text("Cambiar avatar", color = NaranjaIris)
                }

                Spacer(Modifier.height(24.dp))

                ProfileInfoRow(label = "Nombre", value = "${userProfile.nombre} ${userProfile.apellido}", onClick = { showEditDialog = true })
                ProfileInfoRow(label = "Email", value = userProfile.email, canEdit = false)
                ProfileInfoRow(label = "Movimiento Favorito", value = userProfile.movimientoFavorito, onClick = { showEditDialog = true })
                ProfileInfoRow(label = "Biografía", value = userProfile.biografia, onClick = { showEditDialog = true })

                Spacer(Modifier.height(24.dp))

                Text("Última nota de la Bitácora", style = MaterialTheme.typography.titleMedium, color = Color.White)
                Spacer(Modifier.height(8.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    color = Color.DarkGray.copy(alpha = 0.3f),
                    tonalElevation = 2.dp
                ) {
                    Text(
                        text = userProfile.bitacoraNote.ifEmpty { "Aún no tienes notas en tu bitácora." },
                        modifier = Modifier.padding(16.dp),
                        color = Color.White.copy(alpha = 0.8f),
                        maxLines = 4
                    )
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(uiState.error ?: "No se pudo cargar el perfil.", color = Color.Gray)
            }
        }
    }

    if (showEditDialog && userProfile != null) {
        EditProfileDialog(
            profile = userProfile,
            onDismiss = { showEditDialog = false },
            onSave = { newName, newMovement, newBio ->
                vm.updateUserProfile(newName, newMovement, newBio)
                showEditDialog = false
            }
        )
    }
}

@Composable
private fun ProfileInfoRow(label: String, value: String, canEdit: Boolean = true, onClick: () -> Unit = {}) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = label, fontWeight = FontWeight.Bold, color = Color.White)
            if (canEdit) {
                IconButton(onClick = onClick) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color.White.copy(alpha = 0.7f))
                }
            }
        }
        Text(text = value, color = Color.White.copy(alpha = 0.8f), modifier = Modifier.padding(start = 8.dp, top = 4.dp))
    }
}

@Composable
private fun EditProfileDialog(
    profile: UserProfile,
    onDismiss: () -> Unit,
    onSave: (String, String, String) -> Unit
) {
    var name by remember { mutableStateOf(profile.nombre) }
    var movement by remember { mutableStateOf(profile.movimientoFavorito) }
    var bio by remember { mutableStateOf(profile.biografia) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(16.dp), color = IrisFondoGris) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Editar Perfil", style = MaterialTheme.typography.headlineSmall, color = Color.White)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = movement,
                    onValueChange = { movement = it },
                    label = { Text("Movimiento Favorito") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = bio,
                    onValueChange = { bio = it },
                    label = { Text("Biografía") },
                    modifier = Modifier.fillMaxWidth().height(100.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar", color = NaranjaIris)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Button(onClick = { onSave(name, movement, bio) }, colors = ButtonDefaults.buttonColors(containerColor = NaranjaIris)) {
                        Text("Guardar")
                    }
                }
            }
        }
    }
}