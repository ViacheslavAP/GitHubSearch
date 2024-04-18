package ru.perelyginva.githubsearch.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.gson.Gson
import ru.perelyginva.githubsearch.data.model.RepositoryItem
import ru.perelyginva.githubsearch.presentation.AboutScreen
import ru.perelyginva.githubsearch.presentation.ComposeWebView
import ru.perelyginva.githubsearch.presentation.HomeScreen
import ru.perelyginva.githubsearch.presentation.RepositoryDetailsScreen

@Composable
fun NavGraph(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navHostController,
        startDestination = MainDestinations.HOME_ROUTE,
        modifier = modifier
    ) {
        composable(route = MainDestinations.HOME_ROUTE) {
            HomeScreen(navHostController)
        }
        composable(route = MainDestinations.ABOUT) {
            AboutScreen(navHostController)
        }

        composable(route = "${MainDestinations.REPOSITORY_DETAILS_ROUTE}/{jsonString}") { backStackEntry ->
            val jsonString: String? = backStackEntry.arguments?.getString("jsonString")
            val user: RepositoryItem? = Gson().fromJson(jsonString, RepositoryItem::class.java)
            user?.let { it1 -> RepositoryDetailsScreen(it1, navHostController) }
        }

        composable(route = "${MainDestinations.WEB_VIEW_ROUTE}/{url}") { backStackEntry ->
            val url: String? = backStackEntry.arguments?.getString("url")
            ComposeWebView(url.toString())
        }
    }
}