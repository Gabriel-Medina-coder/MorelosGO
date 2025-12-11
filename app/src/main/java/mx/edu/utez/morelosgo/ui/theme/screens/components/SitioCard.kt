package mx.edu.utez.morelosgo.ui.theme.screens.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mx.edu.utez.morelosgo.data.network.model.Sitio

@Composable
fun SitioCard(
    sitio: Sitio,
    onDetails: (Sitio) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = sitio.nombre)
                Text(text = "Horario: ${sitio.horarios}")
                Text(text = "Costo: ${sitio.costos}")
                Text(text = "Tipo: ${sitio.tipo}")
            }
            Button(
                onClick = { onDetails(sitio) }
            ) {
                Text(text = "Ver Detalles")
            }
        }
    }
}
