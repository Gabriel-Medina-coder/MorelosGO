package mx.edu.utez.morelosgo.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlignVerticalBottom
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import mx.edu.utez.morelosgo.R

@Composable
fun PerfilScreen(navController: NavController){
    Column {
        Image(
            painter = painterResource(R.drawable.escudomorelos),
            contentDescription = "Logo"
        )
        Text(text = "Nombre")
        Text(text = "Correo electrónico")

        Row {
            Image(
                imageVector = Icons.Filled.AlignVerticalBottom,
                contentDescription = "Logo"
            )
            Column {
                Text(text = "Datos")
            }
        }

        Row {
            Image(
                imageVector = Icons.Filled.LocationOn,
                contentDescription = "Logo"
            )
            Column {
                Text(text = "Sitios Favoritos")
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PerfilPreview(){
    PerfilScreen(navController = rememberNavController())
}