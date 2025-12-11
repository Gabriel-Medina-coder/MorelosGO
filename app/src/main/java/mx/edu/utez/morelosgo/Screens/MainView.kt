package mx.edu.utez.morelosgo.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import mx.edu.utez.volley4c.ui.components.SpinnerDropdown

@Composable
fun MainView(){

    Column {
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = {
                Row {
                    Text(text = "Buscar sitios")
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search"
                    )
                }
            }
        )

        //Colocar SpinnerDropDown

    }
}

@Composable
@Preview(showBackground = true)
fun MainViewPreview(){
    MainView()
}