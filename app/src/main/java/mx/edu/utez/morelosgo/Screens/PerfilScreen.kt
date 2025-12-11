package mx.edu.utez.morelosgo.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import mx.edu.utez.morelosgo.R
import mx.edu.utez.morelosgo.data.network.model.Favorito
import mx.edu.utez.morelosgo.data.network.repository.FavoritoRepository
import mx.edu.utez.morelosgo.utils.SessionManager

@Composable
fun PerfilScreen(navController: NavController){
    val context = LocalContext.current
    val favoritoRepository = remember { FavoritoRepository(context) }
    
    // Obtener usuario actual de la sesión
    val currentUser = remember { SessionManager.getCurrentUser(context) }
    var favoritosCount by remember { mutableStateOf(0) }
    
    // Cargar cantidad de favoritos
    LaunchedEffect(currentUser) {
        currentUser?.let { user ->
            favoritoRepository.getAll(
                onSuccess = { favoritos ->
                    favoritosCount = favoritos.count { it.idUsuario == user.idUsuario }
                },
                onError = { }
            )
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        
        Image(
            painter = painterResource(R.drawable.escudomorelos),
            contentDescription = "Logo",
            modifier = Modifier.size(100.dp)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Mostrar nombre del usuario actual
        Text(
            text = currentUser?.nombre ?: "Usuario",
            style = MaterialTheme.typography.headlineSmall
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Mostrar correo del usuario actual
        Text(
            text = currentUser?.correo ?: "No hay sesión activa",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Card de Mis Datos
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Datos",
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Mis Datos",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "ID: ${currentUser?.idUsuario ?: "N/A"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Card de Sitios Favoritos
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Sitios Favoritos",
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Sitios Favoritos",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "$favoritosCount favoritos",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Botón de cerrar sesión
        OutlinedButton(
            onClick = {
                // Limpiar sesión
                SessionManager.clearSession(context)
                
                // Navegar al login
                navController.navigate("login") {
                    popUpTo(0) { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Filled.Logout,
                contentDescription = "Cerrar sesión"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Cerrar sesión")
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PerfilPreview(){
    val navController = rememberNavController()
    PerfilScreen(navController)
}