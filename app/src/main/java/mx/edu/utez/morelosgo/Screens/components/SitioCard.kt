package mx.edu.utez.morelosgo.Screens.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import mx.edu.utez.morelosgo.data.network.model.Sitio

@Composable
fun SitioCard(
    sitio: Sitio,
    onDetails: (Sitio) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 5.dp,
                bottom = 5.dp,
                start = 10.dp,
                end = 10.dp
            )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen del sitio
            AsyncImage(
                model = sitio.fotografia,
                contentDescription = "Foto del sitio",
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = sitio.nombre,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Tipo: ${sitio.tipo}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Horarios: ${sitio.horarios}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Costo: ${sitio.costos}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            IconButton(
                onClick = { onDetails(sitio) }
            ) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = "Ver detalles"
                )
            }
        }
    }
}
