package mx.edu.utez.morelosgo.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import mx.edu.utez.morelosgo.Screens.components.SitioCard
import mx.edu.utez.morelosgo.Screens.components.SitioDetailDialog
import mx.edu.utez.morelosgo.data.network.model.Sitio
import mx.edu.utez.morelosgo.data.network.repository.SitioRepository

@Composable
fun MainView(navController: NavController){
    val context = LocalContext.current
    val repository = remember { SitioRepository(context) }
    
    var searchQuery by remember { mutableStateOf("") }
    var sitios by remember { mutableStateOf<List<Sitio>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var selectedSitio by remember { mutableStateOf<Sitio?>(null) }
    
    // Cargar sitios al iniciar
    LaunchedEffect(Unit) {
        isLoading = true
        repository.getAll(
            onSuccess = { listaSitios ->
                sitios = listaSitios
                isLoading = false
            },
            onError = { error ->
                errorMessage = "Error al cargar sitios: $error"
                isLoading = false
            }
        )
    }
    
    // Filtrar sitios por búsqueda
    val filteredSitios = remember(searchQuery, sitios) {
        if (searchQuery.isBlank()) {
            sitios
        } else {
            sitios.filter { 
                it.nombre.contains(searchQuery, ignoreCase = true) ||
                it.tipo.contains(searchQuery, ignoreCase = true)
            }
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        // Barra de búsqueda
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text(text = "Buscar sitios") },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search"
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Lista de sitios
        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            errorMessage != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            filteredSitios.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (searchQuery.isBlank()) 
                            "No hay sitios disponibles" 
                        else 
                            "No se encontraron sitios",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredSitios) { sitio ->
                        SitioCard(
                            sitio = sitio,
                            onInfoClick = { selectedSitio = sitio }
                        )
                    }
                    
                    // Espacio al final
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
    
    // Dialog de detalles
    selectedSitio?.let { sitio ->
        SitioDetailDialog(
            sitio = sitio,
            onDismiss = { selectedSitio = null }
        )
    }
}

@Composable
@Preview(showBackground = true)
fun MainViewPreview(){
    val navController = rememberNavController()
    MainView(navController)
}