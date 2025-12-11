package mx.edu.utez.morelosgo.ui.theme.screens.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import mx.edu.utez.morelosgo.data.network.model.Sitio

@Composable
fun SitioList(
    sitios: List<Sitio>
) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedSitio by remember { mutableStateOf<Sitio?>(null) }

    if (showDialog && selectedSitio != null) {
        SitioDialog(
            sitio = selectedSitio!!,
            onDismiss = {
                showDialog = false
                selectedSitio = null
            }
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(sitios) { sitio ->
            SitioCard(
                sitio = sitio,
                onDetails = {
                    selectedSitio = it
                    showDialog = true
                }
            )
        }
    }
}
