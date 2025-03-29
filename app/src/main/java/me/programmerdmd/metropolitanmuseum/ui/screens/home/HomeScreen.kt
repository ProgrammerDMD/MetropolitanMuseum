package me.programmerdmd.metropolitanmuseum.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import me.programmerdmd.metropolitanmuseum.objects.api.MuseumObject
import me.programmerdmd.metropolitanmuseum.ui.theme.MetropolitanMuseumTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(onSearch: () -> Unit,
               onNavigateToDetail: (objectId: Int, title: String) -> Unit) {
    MetropolitanMuseumTheme {
        Scaffold(
            topBar = { TopBar(onSearch) },
            bottomBar = { BottomBar() }
        ) { innerPadding ->
            ItemsComponent(modifier = Modifier.padding(innerPadding),
                onNavigateToDetail = onNavigateToDetail)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(onSearch: () -> Unit = {}) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {

        },
        actions = {
            IconButton(onClick = onSearch) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search for a museum"
                )
            }
        }
    )
}

@Composable
private fun BottomBar() {
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

        // Trending Button
        NavigationBarItem(
            icon = {
                Icon(imageVector = Icons.AutoMirrored.Filled.TrendingUp, contentDescription = "Trending")
            },
            label = {
                Text("Trending")
            },
            selected = false,
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

@Composable
private fun ItemsComponent(modifier: Modifier = Modifier,
                           viewModel: HomeScreenViewModel = koinViewModel(),
                           onNavigateToDetail: (objectId: Int, title: String) -> Unit) {
    val items by viewModel.itemsFlow.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

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

        item{
            LaunchedEffect(key1 = items.size){
                viewModel.loadMoreItems()
            }
        }
    }
}

@Composable
private fun LoadingComponent(modifier: Modifier = Modifier) {
    Column(modifier = modifier
        .fillMaxWidth()
        .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}

private fun joinStrings(strings: List<String>): String {
    return strings.filter { it.isNotEmpty() }.joinToString(", ")
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MuseumCard(item: MuseumObject, onClick: () -> Unit) {
    ElevatedCard(
        modifier = Modifier.clickable {
            onClick()
        },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = joinStrings(listOf(item.date, item.artist)),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (item.image.isNotBlank()) {
                GlideImage(
                    model = item.image,
                    contentDescription = ""
                )
            }
        }
    }
}