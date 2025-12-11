package mx.edu.utez.morelosgo.Screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {
// El 'cerebro' que controla en qué pantalla estamos
    val navController = rememberNavController()

    // El contenedor que aloja todas las pantallas
    NavHost(
        navController = navController,
        // La primera pantalla que se muestra al iniciar la app
        startDestination = "login"
    ){

        // --- Pantallas de Autenticación (Nivel 1) ---

        composable("login"){
            // A la pantalla de Login le damos el cerebro (navController)
            LoginScreen(navController)
        }

        composable("signin"){
            // A la pantalla de Registro le damos el cerebro
            SignInScreen(navController)
        }

        // --- Pantalla Principal (Después de Iniciar Sesión) ---

        composable("main"){
            // MainScreen es ahora el punto central después del login.
            // Contiene su propia barra de navegación inferior.
            MainScreen()
        }

        // Ya no necesitamos "map" y "perfil" aquí, porque MainScreen
        // se encarga de su propia navegación interna.
    }
}