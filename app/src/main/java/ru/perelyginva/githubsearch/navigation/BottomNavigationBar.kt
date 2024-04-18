package ru.perelyginva.githubsearch.navigation

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun BottomNavigationBar(navController: NavHostController) {

    val currentRoute = navController.currentDestination?.route

    BottomNavigation {

        BottomNavigationItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = currentRoute == MainDestinations.HOME_ROUTE,
            onClick = { navController.navigate(MainDestinations.HOME_ROUTE) }
        )

        BottomNavigationItem(
            icon = { Icon(Icons.Default.Info, contentDescription = "About") },
            label = { Text("About") },
            selected = currentRoute == MainDestinations.ABOUT,
            onClick = { navController.navigate(MainDestinations.ABOUT) }
        )

    }
}