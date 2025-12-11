package mx.edu.utez.volley4c.ui.components

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpinnerDropdown(texto: String, lista: List<String>, onSelectedItem: (String) -> Unit) {
    var estado by remember { mutableStateOf(false) }
    var seleccion by remember { mutableStateOf("")}

    ExposedDropdownMenuBox(
        expanded = estado,
        onExpandedChange = { estado = !estado }
    ) {
        TextField(
            value = seleccion,
            onValueChange = {},
            label = { Text(texto) },
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(estado) },
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
        )
        ExposedDropdownMenu(
            expanded = estado,
            onDismissRequest = { estado = false }
        ) {
            lista.forEach { elemento ->
                DropdownMenuItem(
                    text = { Text(text = elemento) },
                    onClick = {
                        onSelectedItem(elemento)
                        seleccion = elemento
                        estado = false
                    }
                )
            }
            //MenuItem
        }
    }
}