package com.example.bizcore.screen

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.bizcore.models.Producto
import com.example.bizcore.models.ProductoResumen
import com.example.bizcore.service.RetrofitClient
import com.example.bizcore.utils.SessionData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    val fakeRootNavController = rememberNavController()
    MainScreen(rootNavController = fakeRootNavController)
}

@Composable
fun MainScreen(modifier: Modifier = Modifier, rootNavController: NavHostController) {
    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(bottomNavController) }
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = "inicio",
            modifier = modifier.padding(innerPadding)
        ) {
            composable("inicio") { MainContent() }
            composable("perfil") {
                PerfilScreen(
                    onCerrarSesionClick = {
                        SessionData.clear()
                        rootNavController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
            composable("buscar") {
                // pantalla buscar
            }
        }
    }
}



@Composable
fun WelcomeText(name: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Bienvenido,",
            style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray),
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = name,
            style = MaterialTheme.typography.titleLarge.copy(color = Color.Black)
        )
    }
}

@Composable
fun MainContent(modifier: Modifier = Modifier) {
    val nombreUsuario = SessionData.nombresUsuario ?: "Invitado"

    var resumen by remember { mutableStateOf<ProductoResumen?>(null) }
    var productos by remember { mutableStateOf<List<Producto>>(emptyList()) }

    LaunchedEffect(true) {
        try {
            val resumenResponse = withContext(Dispatchers.IO) {
                RetrofitClient.apiService.obtenerResumen().execute()
            }
            if (resumenResponse.isSuccessful) {
                resumen = resumenResponse.body()
            }

            val productosResponse = withContext(Dispatchers.IO) {
                RetrofitClient.apiService.obtenerUltimasActualizaciones().execute()
            }
            if (productosResponse.isSuccessful) {
                productos = productosResponse.body() ?: emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        WelcomeText(name = nombreUsuario)

        Spacer(modifier = Modifier.height(16.dp))

        // Tarjeta de resumen
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (resumen != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SummaryItemWithColoredBackground(
                            icon = Icons.Default.Inventory,
                            label = "Totales",
                            count = resumen!!.total.toString(),
                            iconColor = Color(0xFFFFC107)
                        )
                        SummaryItemWithColoredBackground(
                            icon = Icons.Default.Warning,
                            label = "Agotados",
                            count = resumen!!.agotados.toString(),
                            iconColor = Color(0xFFEF5350)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    SummaryItemWithColoredBackground(
                        icon = Icons.Default.Block,
                        label = "Inactivos",
                        count = resumen!!.inactivos.toString(),
                        iconColor = Color.Gray
                    )
                } else {
                    CircularProgressIndicator()
                }
            }
        }

        // Título de actualizaciones
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Últimas actualizaciones en el sistema",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            color = Color.Black
        )

        // Lista dinámica con scroll
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(productos) { producto ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Inventory,
                        contentDescription = "Producto",
                        tint = Color(0xFF66BB6A),
                        modifier = Modifier
                            .size(40.dp)
                            .padding(end = 8.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = producto.nombre.ifBlank { "Producto sin nombre" },
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "SKU ${producto.sku.ifBlank { "Desconocido" }}",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.Gray
                        )
                    }

                    val hora = try {
                        val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                        val horaFormat = SimpleDateFormat("hh:mm a", Locale("es"))
                        val date = isoFormat.parse(producto.fechaRegistro)
                        horaFormat.format(date ?: Date())
                    } catch (e: Exception) {
                        "Sin hora"
                    }

                    Text(
                        text = hora,
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.Gray
                    )
                }
            }
        }

    }
}




@Composable
fun SummaryItemWithColoredBackground(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    count: String,
    iconColor: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(5.dp)
            .size(100.dp)
            .background(iconColor, shape = RoundedCornerShape(16.dp))
            .padding(2.dp)
    ) {
        Icon(
            icon,
            contentDescription = label,
            tint = Color.White,
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = Color.White,
            maxLines = 1
        )
        Text(
            text = count,
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            maxLines = 1
        )
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") },
            selected = currentRoute == "inicio",
            onClick = {
                navController.navigate("inicio") {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
            label = { Text("Buscar") },
            selected = currentRoute == "buscar",
            onClick = {
                navController.navigate("buscar") {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
            label = { Text("Perfil") },
            selected = currentRoute == "perfil",
            onClick = {
                navController.navigate("perfil") {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
    }
}

