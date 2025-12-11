package mx.edu.utez.morelosgo.Screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Main : BottomBarScreen("main", "Inicio", Icons.Default.Home)
    object Map : BottomBarScreen("map", "Mapa", Icons.Default.LocationOn)
    object Profile : BottomBarScreen("perfil", "Perfil", Icons.Default.Person)
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomBar(navController = navController) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            BottomNavGraph(navController = navController)
        }
    }
}

@Composable
fun BottomBar(navController: NavController) {
    val screens = listOf(
        BottomBarScreen.Main,
        BottomBarScreen.Map,
        BottomBarScreen.Profile,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomAppBar {
        screens.forEach { screen ->
            NavigationBarItem(
                label = { Text(text = screen.title) },
                icon = { Icon(imageVector = screen.icon, contentDescription = "Navigation Icon") },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Main.route
    ) {
        composable(route = BottomBarScreen.Main.route) {
            MainView(navController)
        }
        composable(route = BottomBarScreen.Map.route) {
            MapView(navController)
        }
        composable(route = BottomBarScreen.Profile.route) {
            PerfilScreen(navController)
        }
    }
}