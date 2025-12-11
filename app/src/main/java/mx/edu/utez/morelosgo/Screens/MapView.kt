package mx.edu.utez.morelosgo.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun MapView(navController: NavController){
    Column {
        // El contenido del mapa irá aquí.
        // Por ahora, dejamos un texto de ejemplo.
        Text(text = "Vista del Mapa")

        Row {
            // Este botón podría usarse para un filtro específico del mapa
            Button(onClick = {}) {
                Text(text = "Popular")
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun MapPreview(){
    MapView(navController = rememberNavController())
}