package com.bot2000.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ToggleOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Bot2000App(
                onOpenOverlay = {
                    val serviceIntent = Intent(this, FloatingPanelService::class.java)
                    startService(serviceIntent)
                }
            )
        }
    }
}

@Composable
fun Bot2000App(onOpenOverlay: () -> Unit) {
    val chatMessages = remember {
        mutableStateListOf(
            ChatMessage("assistant", "BOT2000 inicializado. Lista para trabajar con IA y comandos."),
            ChatMessage("assistant", "Versión base Android 13: chat + terminal + panel flotante listo."),
        )
    }
    var commandText by remember { mutableStateOf("") }

    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            background = Color(0xFF090B12),
            surface = Color(0xFF10151E),
            primary = Color(0xFF66D9EF),
            secondary = Color(0xFF2F3B4B),
            onBackground = Color(0xFFE6F1FF),
            onSurface = Color(0xFFE6F1FF),
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFF090B12)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TopBar(onOpenOverlay = onOpenOverlay)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ChatPanel(messages = chatMessages)
                    TerminalPanel(
                        commandText = commandText,
                        onCommandTextChange = { commandText = it },
                        onSendCommand = {
                            val trimmed = commandText.trim()
                            if (trimmed.isNotEmpty()) {
                                chatMessages.add(ChatMessage("user", trimmed))
                                chatMessages.add(ChatMessage("assistant", "Ejecutando: $trimmed\nSalida simulada: comando aceptado por el sandbox de BOT2000."))
                                commandText = ""
                            }
                        }
                    )
                }
                StatusPanel()
            }
        }
    }
}

@Composable
fun TopBar(onOpenOverlay: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF10151E), RoundedCornerShape(18.dp))
            .padding(horizontal = 18.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "BOT2000",
                color = Color(0xFFE6F1FF),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Android 13 • MVP • IA + Terminal",
                color = Color(0xFF8BB0D9),
                fontSize = 12.sp
            )
        }

        Button(
            onClick = onOpenOverlay,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F3B4B))
        ) {
            Icon(Icons.Default.ToggleOn, contentDescription = "panel flotante")
            Box(modifier = Modifier.width(8.dp))
            Text("Panel")
        }
    }
}

@Composable
fun ChatPanel(messages: List<ChatMessage>) {
    Column(
        modifier = Modifier
            .weight(1f)
            .height(420.dp)
            .background(Color(0xFF10151E), RoundedCornerShape(18.dp))
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Bolt, contentDescription = null, tint = Color(0xFF66D9EF))
            Box(modifier = Modifier.width(8.dp))
            Text(
                text = "Asistente IA",
                color = Color(0xFFE6F1FF),
                fontWeight = FontWeight.SemiBold
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages) { message ->
                val alignment = if (message.role == "user") Alignment.End else Alignment.Start
                val color = if (message.role == "user") Color(0xFF2F3B4B) else Color(0xFF0D1117)
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = alignment) {
                    Text(
                        text = message.text,
                        modifier = Modifier
                            .background(color, RoundedCornerShape(14.dp))
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        color = Color(0xFFE6F1FF),
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
fun TerminalPanel(
    commandText: String,
    onCommandTextChange: (String) -> Unit,
    onSendCommand: () -> Unit
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .height(420.dp)
            .background(Color(0xFF10151E), RoundedCornerShape(18.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color(0xFF66D9EF))
            Box(modifier = Modifier.width(8.dp))
            Text(
                text = "Terminal",
                color = Color(0xFFE6F1FF),
                fontWeight = FontWeight.SemiBold
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color(0xFF0D1117), RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            Text(
                text = "root@bot2000:~$ status\nSistema: Android 13\nIA: conectada\nSandbox: activo",
                color = Color(0xFF8BB0D9),
                fontSize = 12.sp
            )
        }

        OutlinedTextField(
            value = commandText,
            onValueChange = onCommandTextChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Escribe un comando…") },
            singleLine = true
        )

        Button(
            onClick = onSendCommand,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF66D9EF))
        ) {
            Text("Enviar", color = Color(0xFF090B12), fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun StatusPanel() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF10151E), RoundedCornerShape(18.dp))
            .padding(horizontal = 18.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        StatusChip(text = "IA", active = true)
        StatusChip(text = "Terminal", active = true)
        StatusChip(text = "Panel flotante", active = true)
        StatusChip(text = "Descargas", active = false)
    }
}

@Composable
fun StatusChip(text: String, active: Boolean) {
    Row(
        modifier = Modifier
            .background(
                if (active) Color(0xFF2F3B4B) else Color(0xFF151B24),
                RoundedCornerShape(999.dp)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .background(if (active) Color(0xFF66D9EF) else Color(0xFF8BB0D9), RoundedCornerShape(999.dp))
                .width(8.dp)
                .height(8.dp)
        )
        Box(modifier = Modifier.width(8.dp))
        Text(text = text, color = Color(0xFFE6F1FF), fontSize = 12.sp)
    }
}

data class ChatMessage(
    val role: String,
    val text: String
)
