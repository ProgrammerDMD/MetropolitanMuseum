package me.programmerdmd.metropolitanmuseum

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import me.programmerdmd.metropolitanmuseum.detail.DetailRoute
import me.programmerdmd.metropolitanmuseum.detail.DetailScreen
import me.programmerdmd.metropolitanmuseum.home.HomeRoute
import me.programmerdmd.metropolitanmuseum.home.HomeScreen
import me.programmerdmd.metropolitanmuseum.search.SearchRoute
import me.programmerdmd.metropolitanmuseum.search.SearchScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Navigation()
        }
    }
}

@Composable
fun BottomNavigationBar() {
    NavigationBar {
        // Home Button
        NavigationBarItem(
            icon = {
                Icon(imageVector = Icons.Filled.Home, contentDescription = "Home")
            },
            label = {
                Text("Home")
            },
            selected = true,
            onClick = {}
        )

        // Favorites Button
        NavigationBarItem(
            icon = {
                Icon(imageVector = Icons.Filled.StarBorder, contentDescription = "Favorites")
            },
            label = {
                Text("Favorites")
            },
            selected = false,
            onClick = {}
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Navigation() {
    val navController = rememberNavController()
    Scaffold(bottomBar = {
        BottomNavigationBar()
    }) {
        NavHost(navController = navController, startDestination = HomeRoute) {
            composable<HomeRoute> {
                HomeScreen(onSearch = {
                    navController.navigate(route = SearchRoute)
                })
            }
            composable<SearchRoute> {
                SearchScreen()
            }
            composable<DetailRoute> {
                DetailScreen()
            }
        }
    }
}