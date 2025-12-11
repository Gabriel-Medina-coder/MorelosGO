package mx.edu.utez.morelosgo.Screens.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import mx.edu.utez.morelosgo.data.network.model.Sitio

@Composable
fun SitioList(
    sitios: List<Sitio>,
    isFavorite: (Int) -> Boolean,
    onDetails: (Sitio) -> Unit,
    onFavoriteToggle: (Sitio) -> Unit
) {
    LazyColumn {
        items(sitios) { sitio ->
            SitioCard(
                sitio = sitio,
                isFavorite = isFavorite(sitio.idSitio),
                onDetails = onDetails,
                onFavoriteToggle = onFavoriteToggle
            )
        }
    }
}
