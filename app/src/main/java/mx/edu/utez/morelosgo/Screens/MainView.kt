package mx.edu.utez.morelosgo.Screens

import androidx.compose.foundation.layout.*
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
import mx.edu.utez.morelosgo.Screens.components.SitioList
import mx.edu.utez.morelosgo.data.network.model.Favorito
import mx.edu.utez.morelosgo.data.network.model.Sitio
import mx.edu.utez.morelosgo.data.network.repository.FavoritoRepository
import mx.edu.utez.morelosgo.data.network.repository.SitioRepository
import mx.edu.utez.morelosgo.utils.SessionManager

@Composable
fun MainView(navController: NavController){
    val context = LocalContext.current
    val sitioRepository = remember { SitioRepository(context) }
    val favoritoRepository = remember { FavoritoRepository(context) }
    
    var searchQuery by remember { mutableStateOf("") }
    var sitios by remember { mutableStateOf<List<Sitio>>(emptyList()) }
    var favoritos by remember { mutableStateOf<List<Favorito>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var selectedSitio by remember { mutableStateOf<Sitio?>(null) }
    
    // Cargar sitios al iniciar
    LaunchedEffect(Unit) {
        isLoading = true
        sitioRepository.getAll(
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
    
    // Cargar favoritos del usuario actual
    LaunchedEffect(Unit) {
        val currentUserId = SessionManager.getUserId(context)
        if (currentUserId != 0) {
            favoritoRepository.getAll(
                onSuccess = { listaFavoritos ->
                    favoritos = listaFavoritos.filter { it.idUsuario == currentUserId }
                },
                onError = { }
            )
        }
    }
    
    // Función para verificar si un sitio es favorito
    fun isFavorite(idSitio: Int): Boolean {
        return favoritos.any { it.idSitio == idSitio }
    }
    
    // Función para agregar/quitar favorito
    fun toggleFavorite(sitio: Sitio) {
        val esFavorito = isFavorite(sitio.idSitio)
        val currentUserId = SessionManager.getUserId(context)
        
        if (currentUserId == 0) {
            return
        }
        
        if (esFavorito) {
            // Remover favorito
            val favorito = favoritos.find { it.idSitio == sitio.idSitio }
            favorito?.let {
                favoritoRepository.delete(
                    idFavorito = it.idFavorito,
                    onSuccess = {
                        // Recargar favoritos
                        favoritoRepository.getAll(
                            onSuccess = { listaFavoritos ->
                                favoritos = listaFavoritos.filter { it.idUsuario == currentUserId }
                            },
                            onError = { }
                        )
                    },
                    onError = { }
                )
            }
        } else {
            // Agregar favorito
            val nuevoFavorito = Favorito(
                idFavorito = 0,
                idUsuario = currentUserId,
                idSitio = sitio.idSitio
            )
            favoritoRepository.create(
                favorito = nuevoFavorito,
                onSuccess = {
                    // Recargar favoritos
                    favoritoRepository.getAll(
                        onSuccess = { listaFavoritos ->
                            favoritos = listaFavoritos.filter { it.idUsuario == currentUserId }
                        },
                        onError = { }
                    )
                },
                onError = { }
            )
        }
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
            .padding(16.dp)
    ) {
        // Barra de búsqueda
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Buscar sitios") },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Buscar"
                )
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Lista de sitios o estados de carga/error
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
                        text = errorMessage ?: "Error desconocido",
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
                SitioList(
                    sitios = filteredSitios,
                    isFavorite = { idSitio -> isFavorite(idSitio) },
                    onDetails = { sitio ->
                        selectedSitio = sitio
                    },
                    onFavoriteToggle = { sitio ->
                        toggleFavorite(sitio)
                    }
                )
            }
        }
    }
    
    // Diálogo de detalles
    selectedSitio?.let { sitio ->
        AlertDialog(
            onDismissRequest = { selectedSitio = null },
            title = { Text(sitio.nombre) },
            text = {
                Column {
                    Text("Tipo: ${sitio.tipo}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Horarios: ${sitio.horarios}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Costo: ${sitio.costos}")
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedSitio = null }) {
                    Text("Cerrar")
                }
            }
        )
    }
}

@Composable
@Preview(showBackground = true)
fun MainViewPreview(){
    val navController = rememberNavController()
    MainView(navController)
}