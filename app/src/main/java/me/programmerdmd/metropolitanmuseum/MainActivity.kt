package me.programmerdmd.metropolitanmuseum

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import me.programmerdmd.metropolitanmuseum.ui.screens.detail.DetailRoute
import me.programmerdmd.metropolitanmuseum.ui.screens.detail.DetailScreen
import me.programmerdmd.metropolitanmuseum.ui.screens.home.HomeRoute
import me.programmerdmd.metropolitanmuseum.ui.screens.home.HomeScreen
import me.programmerdmd.metropolitanmuseum.ui.screens.search.SearchRoute
import me.programmerdmd.metropolitanmuseum.ui.screens.search.SearchScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Navigation()
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = HomeRoute,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }) {
        composable<HomeRoute> {
            HomeScreen(onSearch = {
                navController.navigate(route = SearchRoute)
            })
        }
        composable<SearchRoute> {
            SearchScreen(onNavigateBack = {
                navController.popBackStack()
            }, onNavigateToDetail = { objectId ->
                run {
                    navController.navigate(route = DetailRoute(objectId))
                }
            })
        }
        composable<DetailRoute> {
            DetailScreen()
        }
    }
}