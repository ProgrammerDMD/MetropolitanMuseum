package me.programmerdmd.metropolitanmuseum.ui.screens.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.programmerdmd.metropolitanmuseum.ui.screens.home.LoadingComponent
import me.programmerdmd.metropolitanmuseum.ui.screens.home.MuseumCard
import me.programmerdmd.metropolitanmuseum.ui.theme.MetropolitanMuseumTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(onNavigateBack: () -> Unit,
                 onNavigateToDetail: (objectId: Int, title: String) -> Unit) {
    MetropolitanMuseumTheme {
        Scaffold(
            topBar = {
                TopBar(onNavigateBack)
            },
        ) { innerPadding ->
            ItemsComponent(modifier = Modifier.padding(innerPadding),
                onNavigateToDetail = onNavigateToDetail)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(onNavigateBack: () -> Unit, viewModel: SearchViewModel = koinViewModel()) {
    var text by remember { mutableStateOf(viewModel.queryFlow.value.query) }
    val focusManager = LocalFocusManager.current

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = text,
                singleLine = true,
                placeholder = { Text(text = "Search for an object") },
                onValueChange = { newText ->
                    run {
                        text = newText
                        viewModel.search(text)
                    }
                },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent
                ),
                trailingIcon = {
                    if (text.isNotEmpty()) {
                        IconButton(onClick = {
                            text = ""
                            viewModel.search(text)
                            focusManager.clearFocus()
                        }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear Query"
                            )
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    viewModel.search(text)
                    focusManager.clearFocus()
                })
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Navigate to Home"
                )
            }
        }
    )
}

@Composable
private fun ItemsComponent(modifier: Modifier = Modifier,
                   viewModel: SearchViewModel = koinViewModel(),
                   onNavigateToDetail: (objectId: Int, title: String) -> Unit) {
    val objects by viewModel.results.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val isLastPage by viewModel.isLastPage.collectAsStateWithLifecycle()

    LazyVerticalGrid(modifier = modifier, columns = GridCells.Adaptive(minSize = 256.dp),
        contentPadding = PaddingValues(18.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(objects) { index, item ->
            MuseumCard(item, {
                onNavigateToDetail(item.id, item.title)
            })
        }

        if (isLoading) {
            item {
                LoadingComponent()
            }
        }

        if (!isLoading && isLastPage && objects.isEmpty()) {
            item {
                Text(modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                    text = "No results found!",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall)
            }
        }

        if (!isLoading && !isLastPage) {
            item {
                LaunchedEffect(key1 = objects.size){
                    viewModel.loadMoreItems()
                }
            }
        }
    }
}