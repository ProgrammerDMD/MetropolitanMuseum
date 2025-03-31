package me.programmerdmd.metropolitanmuseum.ui.screens.trending

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import me.programmerdmd.metropolitanmuseum.ui.screens.home.BottomBar
import me.programmerdmd.metropolitanmuseum.ui.screens.home.TopBar
import me.programmerdmd.metropolitanmuseum.ui.theme.MetropolitanMuseumTheme

@Composable
fun TrendingScreen(
    onSearch: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToFavorites: () -> Unit,
    onNavigateToDetail: (objectId: Int, title: String) -> Unit
) {
    MetropolitanMuseumTheme {
        Scaffold(
            topBar = { TopBar(onSearch) },
            bottomBar = { BottomBar(
                route = "Trending",
                onNavigateToHome = onNavigateToHome,
                onNavigateToTrending = {},
                onNavigateToFavorites = onNavigateToFavorites,
            ) }
        ) { innerPadding ->

        }
    }
}