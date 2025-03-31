package me.programmerdmd.metropolitanmuseum.ui.screens.favorites

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import me.programmerdmd.metropolitanmuseum.ui.screens.home.BottomBar
import me.programmerdmd.metropolitanmuseum.ui.screens.home.TopBar
import me.programmerdmd.metropolitanmuseum.ui.theme.MetropolitanMuseumTheme

@Composable
fun FavoritesScreen(
    onSearch: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToTrending: () -> Unit,
    onNavigateToDetail: (objectId: Int, title: String) -> Unit
) {
    MetropolitanMuseumTheme {
        Scaffold(
            topBar = { TopBar(onSearch) },
            bottomBar = { BottomBar(
                route = "Favorites",
                onNavigateToHome = onNavigateToHome,
                onNavigateToTrending = onNavigateToTrending,
                onNavigateToFavorites = {}
            ) }
        ) { innerPadding ->

        }
    }
}