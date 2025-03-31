package me.programmerdmd.metropolitanmuseum

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import me.programmerdmd.metropolitanmuseum.ui.screens.detail.DetailRoute
import me.programmerdmd.metropolitanmuseum.ui.screens.detail.DetailScreen
import me.programmerdmd.metropolitanmuseum.ui.screens.favorites.FavoritesRoute
import me.programmerdmd.metropolitanmuseum.ui.screens.favorites.FavoritesScreen
import me.programmerdmd.metropolitanmuseum.ui.screens.home.HomeRoute
import me.programmerdmd.metropolitanmuseum.ui.screens.home.HomeScreen
import me.programmerdmd.metropolitanmuseum.ui.screens.search.SearchRoute
import me.programmerdmd.metropolitanmuseum.ui.screens.search.SearchScreen
import me.programmerdmd.metropolitanmuseum.ui.screens.trending.TrendingRoute
import me.programmerdmd.metropolitanmuseum.ui.screens.trending.TrendingScreen

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
    val onSearch = {
        navController.navigate(route = SearchRoute)
    }

    val navigateBottomBar: (Any) -> Unit = { route ->
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }

            launchSingleTop = true
            restoreState = true
        }
    }

    val onNavigateToHome = { navigateBottomBar(HomeRoute) }
    val onNavigateToTrending = { navigateBottomBar(TrendingRoute) }
    val onNavigateToFavorites = { navigateBottomBar(FavoritesRoute) }

    val onNavigateBack: () -> Unit = {
        navController.popBackStack()
    }

    val onNavigateToDetail: (Int, String) -> Unit = { objectId, title ->
        run {
            navController.navigate(route = DetailRoute(objectId, title))
        }
    }

    NavHost(
        navController = navController,
        startDestination = HomeRoute,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }) {
        composable<HomeRoute> {
            HomeScreen(onSearch = onSearch,
                onNavigateToDetail = onNavigateToDetail,
                onNavigateToTrending = onNavigateToTrending,
                onNavigateToFavorites = onNavigateToFavorites
            )
        }
        composable<SearchRoute> {
            SearchScreen(
                onNavigateBack = onNavigateBack,
                onNavigateToDetail = onNavigateToDetail
            )
        }
        composable<DetailRoute> {
            DetailScreen(onNavigateBack = onNavigateBack)
        }
        composable<FavoritesRoute> {
            FavoritesScreen(
                onSearch = onSearch,
                onNavigateToHome = onNavigateToHome,
                onNavigateToTrending = onNavigateToTrending,
                onNavigateToDetail = onNavigateToDetail
            )
        }
        composable<TrendingRoute> {
            TrendingScreen(
                onSearch = onSearch,
                onNavigateToHome = onNavigateToHome,
                onNavigateToFavorites = onNavigateToFavorites,
                onNavigateToDetail = onNavigateToDetail
            )
        }
    }
}