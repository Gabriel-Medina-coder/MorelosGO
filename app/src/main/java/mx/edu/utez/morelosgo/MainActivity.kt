package mx.edu.utez.morelosgo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import mx.edu.utez.morelosgo.Screens.MorelosGoScreen
import mx.edu.utez.morelosgo.ui.theme.MorelosGoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MorelosGoTheme {
                MorelosGoScreen()
            }
        }
    }
}