package me.programmerdmd.metropolitanmuseum.ui.screens.trending

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.programmerdmd.metropolitanmuseum.ui.screens.home.BottomBar
import me.programmerdmd.metropolitanmuseum.ui.screens.home.HomeScreenViewModel
import me.programmerdmd.metropolitanmuseum.ui.screens.home.LoadingComponent
import me.programmerdmd.metropolitanmuseum.ui.screens.home.MuseumCard
import me.programmerdmd.metropolitanmuseum.ui.screens.home.TopBar
import me.programmerdmd.metropolitanmuseum.ui.theme.MetropolitanMuseumTheme
import org.koin.androidx.compose.koinViewModel

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
            ItemsComponent(
                modifier = Modifier.padding(innerPadding),
                onNavigateToDetail = onNavigateToDetail
            )
        }
    }
}

@Composable
private fun ItemsComponent(modifier: Modifier = Modifier,
                           viewModel: TrendingScreenViewModel = koinViewModel(),
                           onNavigateToDetail: (objectId: Int, title: String) -> Unit) {
    val items by viewModel.itemsFlow.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val isLastPage by viewModel.isLastPage.collectAsStateWithLifecycle()

    LazyVerticalGrid(modifier = modifier, columns = GridCells.Adaptive(minSize = 256.dp),
        contentPadding = PaddingValues(18.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(items) { index, item ->
            MuseumCard(item, {
                onNavigateToDetail(item.id, item.title)
            })
        }

        if (isLoading) {
            item {
                LoadingComponent()
            }
        }

        if (!isLoading && !isLastPage) {
            item {
                LaunchedEffect(key1 = items.size) {
                    viewModel.loadMoreItems()
                }
            }
        }
    }
}