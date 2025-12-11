package mx.edu.utez.morelosgo.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import mx.edu.utez.morelosgo.R

@Composable
fun LoginScreen(navController: NavController) {
    Column() {
        Image(
            painter = painterResource(R.drawable.escudomorelos),
            contentDescription = "Logo"
        )
        Text(text = "Iniciar sesión")
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = {
                Row {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = "User"
                    )
                    Text("Usuario")
                }
            }
        )
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = {
                Row {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = "Password"
                    )
                    Text("Contraseña")
                }
            }
        )
        Text(
            text = "¿No tienes una cuenta? Registrate",
            modifier = Modifier.clickable {
                navController.navigate("signin")
            }
        )

        Button(onClick = {
            // **IMPORTANTE: Aquí va la lógica de autenticación.**
            // Asumimos éxito y navegamos a la vista principal.
            navController.navigate("main") {
                // Borra la pantalla de login del historial para que el usuario no regrese con el botón atrás
                popUpTo("login") { inclusive = true }
            }
        }) {
            Text(text = "Iniciar sesión")
        }
    }
}

@Composable
@Preview(showBackground = true)
fun LoginPreview(){
    LoginScreen(navController = rememberNavController())
}