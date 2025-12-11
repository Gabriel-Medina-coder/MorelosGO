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
import mx.edu.utez.morelosgo.viewmodel.MapViewModel

@Composable
fun MapView(navController: NavController) {
    val context = LocalContext.current
    val viewModel = remember { MapViewModel(context) }
    
    // Collect states from ViewModel
    val sitios by viewModel.sitios.collectAsState()
    val favoritos by viewModel.favoritos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()
    val selectedSitio by viewModel.selectedSitio.collectAsState()
    
    var permitido by remember { mutableStateOf(false) }
    
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
    
    // Get filtered sitios from ViewModel
    val filteredSitios = remember(selectedFilter, sitios, favoritos) {
        viewModel.getFilteredSitios()
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
                onClick = { viewModel.setFilter("General") }
            )
            FilterButton(
                text = "Favoritos",
                isSelected = selectedFilter == "Favoritos",
                onClick = { viewModel.setFilter("Favoritos") }
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
                    
                    Marker(
                        state = rememberMarkerState(position = position),
                        title = sitio.nombre,
                        snippet = sitio.tipo,
                        icon = BitmapDescriptorFactory.defaultMarker(markerColor),
                        onClick = {
                            viewModel.selectSitio(sitio)
                            true
                        }
                    )
                }
            }
        }
        
        // Diálogo para mostrar detalles del sitio seleccionado
        selectedSitio?.let { sitio ->
            AlertDialog(
                onDismissRequest = { viewModel.dismissDialog() },
                title = {
                    Text(
                        text = sitio.nombre,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                text = {
                    Column {
                        Text(
                            text = "Tipo: ${sitio.tipo}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Horarios: ${sitio.horarios}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Costo: ${sitio.costos}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = { viewModel.dismissDialog() }) {
                        Text("Cerrar")
                    }
                }
            )
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