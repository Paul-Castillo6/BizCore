package com.example.bizcore.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.*
import android.widget.Toast
import com.example.bizcore.models.LoginRequest
import com.example.bizcore.service.RetrofitClient
import androidx.compose.ui.platform.LocalContext
import com.example.bizcore.utils.SessionData

@Composable
fun LoginScreen(modifier: Modifier = Modifier, navController: NavController) {
    val context = LocalContext.current
    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val isFormValid = userName.length == 8 && password.isNotBlank()

    // Colores
    val backgroundColor = Color(0xFF061A36)
    val formBackgroundColor = Color.White

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Bienvenido",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Por favor inicia sesión para que puedas continuar",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.LightGray
            )
            Spacer(modifier = Modifier.height(30.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(formBackgroundColor, shape = RoundedCornerShape(16.dp))
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Usuario", modifier = Modifier.fillMaxWidth(), color = Color.Black)
                Spacer(modifier = Modifier.height(5.dp))
                OutlinedTextField(
                    value = userName,
                    onValueChange = {
                        if (it.length <= 8 && it.all { char -> char.isDigit() }) {
                            userName = it
                        }
                    },
                    label = { Text("DNI") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color.Black
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))
                Text("Password", modifier = Modifier.fillMaxWidth(), color = Color.Black)
                Spacer(modifier = Modifier.height(5.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color.Black
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                val response = RetrofitClient.apiService
                                    .login(LoginRequest(userName, password))
                                    .execute()

                                withContext(Dispatchers.Main) {
                                    if (response.isSuccessful) {
                                        val body = response.body()
                                        if (body != null) {
                                            SessionData.idUsuario = body.idUsuario
                                            SessionData.dniUsuario = body.dni
                                            SessionData.nombresUsuario = body.nombres
                                            SessionData.correoUsuario = body.correo
                                            SessionData.rolUsuario = body.rol
                                            SessionData.rolNombre = body.rolNombre
                                            SessionData.estadoUsuario = body.estado
                                            SessionData.estadoNombre = body.estadoNombre
                                            SessionData.fotoBase64 = body.foto
                                            SessionData.codigoTemporal = body.codigoTemporal.toString()

                                            Toast.makeText(context, body.mensaje, Toast.LENGTH_LONG).show()
                                            navController.navigate("otp")
                                        }
                                    } else {
                                        Toast.makeText(context, "Credenciales inválidas", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } catch (e: Exception) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Error de conexión", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    },
                    enabled = isFormValid,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF003366))
                ) {
                    Text("Iniciar Sesión", color = Color.White)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen(navController = rememberNavController())
}
