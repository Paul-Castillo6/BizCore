package com.example.bizcore.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.bizcore.utils.SessionData
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background

@Composable
fun AuthenScreen(modifier: Modifier = Modifier, navController: NavController) {
    val codeLength = 6
    val otpDigits = remember { List(codeLength) { mutableStateOf("") } }
    val focusRequesters = remember { List(codeLength) { FocusRequester() } }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    val isOtpComplete = otpDigits.all { it.value.length == 1 }

    // Colores
    val backgroundColor = Color(0xFF061A36)
    val fieldBackgroundColor = Color(0xFF1C1C1E)
    val borderColor = Color.White
    val buttonBackground = Color(0xFF2C2C2E)
    val buttonTextColor = Color.White

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
                text = "Autenticación",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Introduce el código de seguridad de 6 dígitos que te hemos enviado",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.LightGray
            )
            Spacer(modifier = Modifier.height(30.dp))

            Text("Código de verificación", modifier = Modifier.fillMaxWidth(), color = Color.White)
            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                otpDigits.forEachIndexed { index, state ->
                    OutlinedTextField(
                        value = state.value,
                        onValueChange = {
                            if (it.length <= 1 && it.all { c -> c.isDigit() }) {
                                state.value = it
                                if (it.isNotEmpty()) {
                                    if (index < codeLength - 1) {
                                        focusRequesters[index + 1].requestFocus()
                                    } else {
                                        focusManager.clearFocus()
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .width(50.dp)
                            .focusRequester(focusRequesters[index]),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = borderColor,
                            unfocusedBorderColor = borderColor,
                            focusedContainerColor = fieldBackgroundColor,
                            unfocusedContainerColor = fieldBackgroundColor
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    val fullCode = otpDigits.joinToString("") { it.value }
                    if (fullCode == SessionData.codigoTemporal) {
                        navController.navigate("principal")
                    } else {
                        Toast.makeText(context, "Código incorrecto", Toast.LENGTH_SHORT).show()
                    }
                },
                enabled = isOtpComplete,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonBackground,
                    contentColor = buttonTextColor,
                    disabledContainerColor = buttonBackground.copy(alpha = 0.5f),
                    disabledContentColor = Color.Gray
                )
            ) {
                Text("Continuar")
            }

            Spacer(modifier = Modifier.height(10.dp))

            TextButton(onClick = { navController.navigate("login") }) {
                Text("Volver al inicio de sesión", color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAuthenScreen() {
    AuthenScreen(navController = rememberNavController())
}
