package mx.edu.utez.morelosgo.Screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import mx.edu.utez.morelosgo.data.network.model.Favorito
import mx.edu.utez.morelosgo.data.network.model.Sitio
import mx.edu.utez.morelosgo.data.network.repository.FavoritoRepository
import mx.edu.utez.morelosgo.data.network.repository.SitioRepository
import mx.edu.utez.morelosgo.utils.SessionManager

@Composable
fun MapView(navController: NavController) {
    val context = LocalContext.current
    val sitioRepository = remember { SitioRepository(context) }
    val favoritoRepository = remember { FavoritoRepository(context) }
    
    var selectedFilter by remember { mutableStateOf("General") }
    var permitido by remember { mutableStateOf(false) }
    var sitios by remember { mutableStateOf<List<Sitio>>(emptyList()) }
    var favoritos by remember { mutableStateOf<List<Favorito>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    
    // Cargar sitios
    LaunchedEffect(Unit) {
        isLoading = true
        sitioRepository.getAll(
            onSuccess = { listaSitios ->
                sitios = listaSitios
                isLoading = false
            },
            onError = {
                isLoading = false
            }
        )
    }
    
    // Cargar favoritos
    LaunchedEffect(Unit) {
        favoritoRepository.getAll(
            onSuccess = { listaFavoritos ->
                favoritos = listaFavoritos
            },
            onError = { }
        )
    }
    
    // Permisos de ubicación
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        permitido = granted
    }
    
    // Verificar y solicitar permisos
    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            permitido = true
        } else {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
    
    // Ubicación inicial centrada en Morelos
    val morelasCenter = LatLng(18.6813, -99.1013)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(morelasCenter, 10f)
    }
    
    // Filtrar sitios según filtro
    val filteredSitios = remember(selectedFilter, sitios, favoritos) {
        when (selectedFilter) {
            "Favoritos" -> {
                val favoritoIds = favoritos.map { it.idSitio }.toSet()
                sitios.filter { it.idSitio in favoritoIds }
            }
            else -> sitios // "General" muestra todos
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
            // No hay usuario logueado
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
                            onSuccess = { favoritos = it },
                            onError = { }
                        )
                    },
                    onError = { }
                )
            }
        } else {
            // Agregar favorito con ID del usuario actual
            val nuevoFavorito = Favorito(
                idFavorito = 0, // Se genera en el servidor
                idUsuario = currentUserId,
                idSitio = sitio.idSitio
            )
            favoritoRepository.create(
                favorito = nuevoFavorito,
                onSuccess = {
                    // Recargar favoritos
                    favoritoRepository.getAll(
                        onSuccess = { favoritos = it },
                        onError = { }
                    )
                },
                onError = { }
            )
        }
    }
    
    // Función para obtener color del marcador según tipo
    fun getMarkerColor(tipo: String): Float {
        return when {
            tipo.contains("Popular", ignoreCase = true) -> BitmapDescriptorFactory.HUE_RED
            tipo.contains("General", ignoreCase = true) -> BitmapDescriptorFactory.HUE_BLUE
            tipo.contains("Cultural", ignoreCase = true) -> BitmapDescriptorFactory.HUE_VIOLET
            tipo.contains("Natural", ignoreCase = true) -> BitmapDescriptorFactory.HUE_GREEN
            tipo.contains("Histórico", ignoreCase = true) -> BitmapDescriptorFactory.HUE_ORANGE
            else -> BitmapDescriptorFactory.HUE_AZURE
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        // Botones de filtro: General y Favoritos
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterButton(
                text = "General",
                isSelected = selectedFilter == "General",
                onClick = { selectedFilter = "General" }
            )
            FilterButton(
                text = "Favoritos",
                isSelected = selectedFilter == "Favoritos",
                onClick = { selectedFilter = "Favoritos" }
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Mapa de Google
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(isMyLocationEnabled = permitido),
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = true,
                    myLocationButtonEnabled = permitido,
                    compassEnabled = true
                )
            ) {
                // Marcadores para cada sitio con coordenadas reales
                filteredSitios.forEach { sitio ->
                    val position = LatLng(sitio.latitud, sitio.longitud)
                    val markerColor = getMarkerColor(sitio.tipo)
                    val esFavorito = isFavorite(sitio.idSitio)
                    
                    MarkerInfoWindowContent(
                        state = rememberMarkerState(position = position),
                        title = sitio.nombre,
                        snippet = sitio.tipo,
                        icon = BitmapDescriptorFactory.defaultMarker(markerColor)
                    ) { marker ->
                        // Info Window personalizado con botón de favorito
                        Card(
                            modifier = Modifier.padding(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {
                                // Header con nombre y botón favorito
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = sitio.nombre,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.weight(1f)
                                    )
                                    
                                    IconButton(
                                        onClick = { toggleFavorite(sitio) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (esFavorito) 
                                                Icons.Filled.Favorite 
                                            else 
                                                Icons.Outlined.FavoriteBorder,
                                            contentDescription = "Favorito",
                                            tint = if (esFavorito) 
                                                MaterialTheme.colorScheme.error 
                                            else 
                                                MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                                
                                Spacer(modifier = Modifier.height(4.dp))
                                
                                Text(
                                    text = "Tipo: ${sitio.tipo}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "Horarios: ${sitio.horarios}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "Costo: ${sitio.costos}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) 
                MaterialTheme.colorScheme.primary 
            else 
                MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier.width(150.dp)
    ) {
        Text(text = text)
    }
}

@Composable
@Preview(showBackground = true)
fun MapViewPreview() {
    val navController = rememberNavController()
    MapView(navController)
}