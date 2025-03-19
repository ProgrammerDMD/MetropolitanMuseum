package me.programmerdmd.metropolitanmuseum.ui.screens.detail

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailScreen(viewModel: DetailScreenViewModel = koinViewModel(),) {
    Text("Test")
}