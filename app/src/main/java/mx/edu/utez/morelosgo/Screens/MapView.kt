package mx.edu.utez.morelosgo.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun MapView(navController: NavController){
    var selectedFilter by remember { mutableStateOf("Todos") }
    
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
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
        
        // Aquí se puede agregar el mapa o lista de sitios filtrados
        Text(text = "Filtro seleccionado: $selectedFilter")
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
fun MapViewPreview(){
    val navController = rememberNavController()
    MapView(navController)
}