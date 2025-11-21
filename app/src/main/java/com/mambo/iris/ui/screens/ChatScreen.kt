package com.mambo.iris.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.mambo.iris.ui.theme.IrisFondoGris
import com.mambo.iris.ui.theme.NaranjaIris
import com.mambo.iris.ui.theme.workSansRegular
import com.mambo.iris.ui.viewmodels.ChatMessage
import com.mambo.iris.ui.viewmodels.ChatViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatContent(chatViewModel: ChatViewModel, onDismiss: () -> Unit) {
    val messages by chatViewModel.messages.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Asistente del Museo") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = IrisFondoGris.copy(alpha = 0.9f),
                    titleContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar Chat", tint = Color.White)
                    }
                }
            )
        },
        containerColor = IrisFondoGris.copy(alpha = 0.9f)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                item { Spacer(modifier = Modifier.height(16.dp)) }
                items(messages) { msg ->
                    MessageBubble(msg)
                    Spacer(modifier = Modifier.height(8.dp))
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
            MessageInput(onSendMessage = { chatViewModel.sendMessage(it) })
        }
    }
}

@Composable
fun ChatDialog(chatViewModel: ChatViewModel, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .height(500.dp) // Give it a fixed height
                .clip(RoundedCornerShape(24.dp))
        ) {
            ChatContent(chatViewModel = chatViewModel, onDismiss = onDismiss)
        }
    }
}

@Composable
fun ChatScreen(chatViewModel: ChatViewModel) {
    ChatContent(chatViewModel = chatViewModel, onDismiss = {})
}

@Composable
fun MessageBubble(message: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .clip(RoundedCornerShape(16.dp)),
            color = if (message.isUser) NaranjaIris.copy(alpha = 0.8f) else Color.DarkGray.copy(alpha = 0.4f)
        ) {
            Text(
                text = message.text,
                color = Color.White,
                modifier = Modifier.padding(16.dp),
                fontSize = 16.sp,
                fontFamily = workSansRegular
            )
        }
    }
}

@Composable
fun MessageInput(onSendMessage: (String) -> Unit) {
    var input by remember { mutableStateOf("") }

    Surface(color = MaterialTheme.colorScheme.surface.copy(alpha = 0.1f)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Pregúntame sobre una obra...") },
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NaranjaIris,
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = NaranjaIris,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )
            Spacer(Modifier.width(8.dp))
            IconButton(
                onClick = {
                    val sendText = input.trim()
                    if (sendText.isNotEmpty()) {
                        onSendMessage(sendText)
                        input = ""
                    }
                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = NaranjaIris,
                    contentColor = Color.White
                )
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Enviar")
            }
        }
    }
}