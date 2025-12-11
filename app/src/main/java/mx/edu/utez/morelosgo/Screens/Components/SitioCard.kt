package mx.edu.utez.morelosgo.Screens.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import mx.edu.utez.morelosgo.R

@Composable
fun SitioCard() {
    Card {
        Column {
            Image(
                painter = painterResource(R.drawable.escudomorelos),
                contentDescription = "Logo"
            )
            Row {
                Text(
                    text = "Nombre"
                )
                Text(
                    text = " | "
                )
                Text(
                    text = "Cultural"
                )
            }
            Button(onClick = {}) {
                Text(text = "Ver")
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SitioCardPreview(){
    SitioCard()
}