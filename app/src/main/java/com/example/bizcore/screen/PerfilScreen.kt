package com.example.bizcore.screen

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bizcore.utils.SessionData

@Composable
fun PerfilScreen(onCerrarSesionClick: () -> Unit = {}) {
    val nombres = SessionData.nombresUsuario ?: ""
    val dni = SessionData.dniUsuario ?: ""
    val correo = SessionData.correoUsuario ?: ""
    val rol = SessionData.rolNombre ?: ""
    val estado = SessionData.estadoNombre ?: ""
    val fotoBase64 = SessionData.fotoBase64

    val bitmap = remember(fotoBase64) {
        try {
            if (!fotoBase64.isNullOrEmpty()) {
                val imageBytes = Base64.decode(fotoBase64, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            } else null
        } catch (e: Exception) {
            null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            )
        } else {
            DefaultUserIcon()
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = nombres,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        PerfilItem(label = "DNI:", value = dni)
        PerfilItem(label = "Correo:", value = correo)
        PerfilItem(label = "Cargo:", value = rol)
        PerfilItem(label = "Estado:", value = estado)

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = onCerrarSesionClick,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            shape = RoundedCornerShape(50)
        ) {
            Text(text = "Cerrar sesión")
        }
    }
}

@Composable
fun DefaultUserIcon() {
    Icon(
        imageVector = Icons.Default.Person,
        contentDescription = "Foto por defecto",
        modifier = Modifier
            .size(100.dp)
            .clip(CircleShape)
            .background(Color.LightGray)
            .padding(16.dp),
        tint = Color.DarkGray
    )
}

@Composable
fun PerfilItem(label: String, value: String) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
            )
        }
    }
}
