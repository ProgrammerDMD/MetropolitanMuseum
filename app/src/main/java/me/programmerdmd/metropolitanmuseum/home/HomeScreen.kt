package me.programmerdmd.metropolitanmuseum.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.StarBorder
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
import androidx.compose.ui.Modifier
import me.programmerdmd.metropolitanmuseum.ui.theme.MetropolitanMuseumTheme

@Composable
fun HomeScreen(onSearch: () -> Unit) {
    MetropolitanMuseumTheme {
        Scaffold(
            topBar = { TopBar(onSearch) },
            bottomBar = { BottomBar() }
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {

            }
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