package ru.perelyginva.githubsearch.navigation

import androidx.compose.foundation.background
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset.Companion.Unspecified
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavHostController
import ru.perelyginva.githubsearch.R

@Composable
fun BottomNavigationBar(navController: NavHostController) {

    val currentRoute = navController.currentDestination?.route

    BottomNavigation(modifier = Modifier,
        backgroundColor = colorResource(id = R.color.grey),
        contentColor = Color.Black
    ) {

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