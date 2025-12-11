package mx.edu.utez.morelosgo.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun MainView(navController: NavController){

    Column {
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = {
                Row {
                    Text(text = "Buscar sitios")
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search"
                    )
                }
            }
        )

        //Colocar SpinnerDropDown
        Row {
            // Estos botones se pueden usar para filtros en el futuro
            Button(onClick = {}) { Text(text = "Todos") }
            Button(onClick = {}) { Text(text = "General") }
            Button(onClick = {}) { Text(text = "Popular") }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun MainViewPreview(){
    MainView(navController = rememberNavController())
}