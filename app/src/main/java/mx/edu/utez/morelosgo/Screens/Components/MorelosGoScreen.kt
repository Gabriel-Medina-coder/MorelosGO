package mx.edu.utez.morelosgo.Screens.Components

import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import mx.edu.utez.morelosgo.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MorelosGoScreen(){
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Certificacion turistica")
                },
                navigationIcon = {
                    Image(
                        painter = painterResource( R.drawable.escudomorelos),
                        contentDescription = "Logo"
                    )
                },
                actions = {
                    IconButton(onClick = {}) {
                        Text(text = "Wallet")
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar (
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Filled.Home,
                            contentDescription = "Home"
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = "Map"
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = "Profile"
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Filled.Wallet,
                            contentDescription = "Profile"
                        )
                    }
                }
            )
        }
    ){ innerPadding ->

    }
}

@Composable
@Preview(showBackground = true)
fun MorelosGoPreview(){
    MorelosGoScreen()
}