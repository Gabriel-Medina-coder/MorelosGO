package mx.edu.utez.morelosgo.Screens.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import mx.edu.utez.morelosgo.data.network.model.Sitio

@Composable
fun SitioList(
    sitios: List<Sitio>,
    onDetails: (Sitio) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(sitios) { sitio ->
            SitioCard(
                sitio = sitio,
                onDetails = onDetails
            )
        }
    }
}
