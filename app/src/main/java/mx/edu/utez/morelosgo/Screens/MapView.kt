package mx.edu.utez.morelosgo.Screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import mx.edu.utez.morelosgo.data.network.model.Sitio
import mx.edu.utez.morelosgo.data.network.repository.SitioRepository

@Composable
fun MapView(navController: NavController) {
    val context = LocalContext.current
    val repository = remember { SitioRepository(context) }
    
    var selectedFilter by remember { mutableStateOf("Todos") }
    var permitido by remember { mutableStateOf(false) }
    var sitios by remember { mutableStateOf<List<Sitio>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedSitio by remember { mutableStateOf<Sitio?>(null) }
    
    // Cargar sitios
    LaunchedEffect(Unit) {
        isLoading = true
        repository.getAll(
            onSuccess = { listaSitios ->
                sitios = listaSitios
                isLoading = false
            },
            onError = {
                isLoading = false
            }
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
    
    // Filtrar sitios según filtro y búsqueda
    val filteredSitios = remember(selectedFilter, sitios, searchQuery) {
        var filtered = when (selectedFilter) {
            "General" -> sitios.filter { it.tipo.contains("General", ignoreCase = true) }
            "Popular" -> sitios.filter { it.tipo.contains("Popular", ignoreCase = true) }
            else -> sitios
        }
        
        if (searchQuery.isNotBlank()) {
            filtered = filtered.filter { 
                it.nombre.contains(searchQuery, ignoreCase = true) ||
                it.tipo.contains(searchQuery, ignoreCase = true)
            }
        }
        
        filtered
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Botones de filtro
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterButton(
                text = "Todos",
                isSelected = selectedFilter == "Todos",
                onClick = { selectedFilter = "Todos" }
            )
            FilterButton(
                text = "General",
                isSelected = selectedFilter == "General",
                onClick = { selectedFilter = "General" }
            )
            FilterButton(
                text = "Popular",
                isSelected = selectedFilter == "Popular",
                onClick = { selectedFilter = "Popular" }
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Mapa de Google
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
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
                    
                    MarkerInfoWindowContent(
                        state = rememberMarkerState(position = position),
                        title = sitio.nombre,
                        snippet = sitio.tipo,
                        icon = BitmapDescriptorFactory.defaultMarker(markerColor),
                        onClick = {
                            selectedSitio = sitio
                            true
                        }
                    ) { marker ->
                        // Info Window personalizado
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
                                Text(
                                    text = sitio.nombre,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
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
        )
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