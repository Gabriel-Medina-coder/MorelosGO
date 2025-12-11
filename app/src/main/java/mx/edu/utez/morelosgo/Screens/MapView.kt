package mx.edu.utez.morelosgo.Screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
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
    
    // Ubicación inicial (Morelos, México)
    val initialLocation = LatLng(18.6813, -99.1013)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialLocation, 10f)
    }
    
    // Filtrar sitios según el filtro seleccionado
    val filteredSitios = remember(selectedFilter, sitios) {
        when (selectedFilter) {
            "General" -> sitios.filter { it.tipo.contains("General", ignoreCase = true) }
            "Popular" -> sitios.filter { it.tipo.contains("Popular", ignoreCase = true) }
            else -> sitios
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
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
        
        Spacer(modifier = Modifier.height(16.dp))
        
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
                    myLocationButtonEnabled = permitido
                )
            ) {
                // Marcadores para cada sitio (si tienen coordenadas)
                filteredSitios.forEach { sitio ->
                    // Nota: Necesitarías latitud y longitud en el modelo Sitio
                    // Por ahora usamos coordenadas de ejemplo en Morelos
                    val position = LatLng(
                        18.6813 + (Math.random() - 0.5) * 0.5,
                        -99.1013 + (Math.random() - 0.5) * 0.5
                    )
                    
                    Marker(
                        state = rememberMarkerState(position = position),
                        title = sitio.nombre,
                        snippet = sitio.tipo
                    )
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