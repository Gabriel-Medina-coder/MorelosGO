package mx.edu.utez.morelosgo.ui.theme.screens.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mx.edu.utez.morelosgo.data.network.model.Sitio

@Composable
fun SitioDialog(
    sitio: Sitio,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = sitio.nombre) },
        text = {
            Column {
                Text(text = "Horarios: ${sitio.horarios}")
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Costos: ${sitio.costos}")
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Tipo: ${sitio.tipo}")
                Spacer(modifier = Modifier.height(4.dp))
                // Fotografia is a local path, leaving as text for now or could be an Image if we knew the loader
                Text(text = "Ruta Foto: ${sitio.fotografia}")
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Reseñas:")
                Text(text = sitio.resenas ?: "Sin reseñas")
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text(text = "Cerrar")
            }
        }
    )
}
