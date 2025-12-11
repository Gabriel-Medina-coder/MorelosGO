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
import mx.edu.utez.morelosgo.viewmodel.MainViewModel

@Composable
fun MainView(navController: NavController){
    val context = LocalContext.current
    val viewModel = remember { MainViewModel(context) }
    
    // Collect states from ViewModel
    val sitios by viewModel.sitios.collectAsState()
    val favoritos by viewModel.favoritos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedSitio by viewModel.selectedSitio.collectAsState()
    
    // Get filtered sitios from ViewModel
    val filteredSitios = remember(searchQuery, sitios) {
        viewModel.getFilteredSitios()
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Barra de búsqueda
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.updateSearchQuery(it) },
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
                    modifier = Modifier.fillMaxSize(),
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
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No se encontraron sitios")
                }
            }
            else -> {
                SitioList(
                    sitios = filteredSitios,
                    isFavorite = { idSitio -> viewModel.isFavorite(idSitio) },
                    onDetails = { sitio -> viewModel.selectSitio(sitio) },
                    onFavoriteToggle = { sitio -> viewModel.toggleFavorite(sitio) }
                )
            }
        }
    }
    
    // Diálogo de detalles
    selectedSitio?.let { sitio ->
        AlertDialog(
            onDismissRequest = { viewModel.dismissDialog() },
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
                TextButton(onClick = { viewModel.dismissDialog() }) {
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