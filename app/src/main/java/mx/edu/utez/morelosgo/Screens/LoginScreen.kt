package mx.edu.utez.morelosgo.Screens

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import mx.edu.utez.morelosgo.R

@Composable
fun LoginScreen() {
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
        Text(text = "¿No tienes una cuenta? Registrate")

        Button(onClick = {}) {
            Text(text = "Iniciar sesión")
        }
    }
}

@Composable
@Preview(showBackground = true)
fun LoginPreview(){
    LoginScreen()
}