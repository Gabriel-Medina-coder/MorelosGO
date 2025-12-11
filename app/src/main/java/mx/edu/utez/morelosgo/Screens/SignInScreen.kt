package mx.edu.utez.morelosgo.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
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
fun SignInScreen(navController: NavController){
    Column() {
        Image(
            painter = painterResource(R.drawable.escudomorelos),
            contentDescription = "Logo"
        )
        Text(text = "Registrate")
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
                        imageVector = Icons.Filled.Email,
                        contentDescription = "Email"
                    )
                    Text("Correo electrónico")
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
            text = "¿Ya tienes una cuenta? Inicia sesión",
            modifier = Modifier.clickable {
                // Navegar a la ruta "login"
                navController.navigate("login")
            }
        )

        Button(onClick = {
            // **IMPORTANTE: Aquí va la lógica de registro.**
            // Asumimos éxito y volvemos a la pantalla de Login.
            navController.navigate("login") {
                // Borra la pantalla de registro de la pila
                popUpTo("signin") { inclusive = true }
            }
        }) {
            Text(text = "Registrar")
        }
    }
    }

@Composable
@Preview(showBackground = true)
fun SignInPreview(){
    SignInScreen(navController = rememberNavController())
}