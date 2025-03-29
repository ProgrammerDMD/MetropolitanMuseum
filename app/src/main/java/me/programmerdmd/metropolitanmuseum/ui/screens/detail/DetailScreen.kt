package me.programmerdmd.metropolitanmuseum.ui.screens.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import me.programmerdmd.metropolitanmuseum.ui.theme.MetropolitanMuseumTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailScreen(onNavigateBack: () -> Unit) {
    MetropolitanMuseumTheme {
        Scaffold(
            topBar = {
                TopBar(onNavigateBack)
            },
        ) { innerPadding ->
            LoadingComponent(modifier = Modifier.padding(innerPadding))
            MuseumComponent(modifier = Modifier.padding(innerPadding))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(onNavigateBack: () -> Unit, viewModel: DetailScreenViewModel = koinViewModel()) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {

        },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Navigate to Search"
                )
            }
        }
    )
}

@Composable
private fun LoadingComponent(modifier: Modifier = Modifier, viewModel: DetailScreenViewModel = koinViewModel()) {
    val searchingState = viewModel.searchingFlow.collectAsStateWithLifecycle()
    if (!searchingState.value) return

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

@Composable
private fun Info(left: String?, right: String?) {
    if (right != null && left != null && right.isNotBlank() && left.isNotBlank()) {
        if (right.isNotBlank() && left.isNotBlank()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = left, modifier = Modifier.weight(0.5f))
                Text(
                    text = right,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
private fun MuseumComponent(modifier: Modifier = Modifier, viewModel: DetailScreenViewModel = koinViewModel()) {
    val searchingState = viewModel.searchingFlow.collectAsStateWithLifecycle()
    if (searchingState.value) return

    val objectState = viewModel.museumFlow.collectAsStateWithLifecycle()
    val museumObject = objectState.value ?: return

    val carouselState = rememberCarouselState {
        museumObject.additionalImages.size
    }
    val scrollState = rememberScrollState()
    Column(modifier = modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(16.dp)
        .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top) {

        // Main Image
        Text(text = museumObject.title,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(0.8f),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(16.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)) {
            GlideImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.5f),
                model = museumObject.image,
                contentDescription = ""
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (museumObject.additionalImages.isNotEmpty()) {
            Text(text = "Additional images",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Medium)
            HorizontalMultiBrowseCarousel(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(221.dp),
                state = carouselState,
                preferredItemWidth = 300.dp,
                itemSpacing = 8.dp
            ) { index ->
                GlideImage(
                    model = museumObject.additionalImages[index],
                    contentDescription = null,
                    modifier = Modifier
                        .height(200.dp)
                        .maskClip(MaterialTheme.shapes.extraLarge),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        Info("Artist", museumObject.artist)
        Info("Date", museumObject.date)
        Info("Medium", museumObject.medium)
        Info("Country", museumObject.country)
        Info("State", museumObject.state)
        Info("Department", museumObject.department)
    }
}