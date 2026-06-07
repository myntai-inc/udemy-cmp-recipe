package jp.myntai.udemy.recipe.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cmprecipe.shared.generated.resources.Res
import cmprecipe.shared.generated.resources.ic_arrow_back
import cmprecipe.shared.generated.resources.ic_favorite
import cmprecipe.shared.generated.resources.ic_favorite_border
import coil3.compose.AsyncImage
import jp.myntai.udemy.recipe.data.model.MealDetail
import jp.myntai.udemy.recipe.ui.component.ErrorContent
import jp.myntai.udemy.recipe.viewmodel.UIState
import org.jetbrains.compose.resources.vectorResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealDetailScreen(
    uiState: UIState<MealDetail>,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onBackClick: () -> Unit,
    onRetry: () -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (uiState) {
                            is UIState.Success -> uiState.data.strMeal
                            else -> "Meal Detail"
                        },
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = vectorResource(Res.drawable.ic_arrow_back),
                            contentDescription = "Back",
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            if (uiState is UIState.Success) {
                FloatingActionButton(onClick = onFavoriteClick) {
                    Icon(
                        imageVector = if (isFavorite) {
                            vectorResource(Res.drawable.ic_favorite)
                        } else {
                            vectorResource(Res.drawable.ic_favorite_border)
                        },
                        contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                    )
                }
            }
        },
    ) { innerPadding ->
        when (uiState) {
            is UIState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }
            is UIState.Error -> {
                ErrorContent(
                    message = uiState.message,
                    onRetry = onRetry,
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                )
            }
            is UIState.Success -> {
                val meal = uiState.data
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState()),
                ) {
                    AsyncImage(
                        model = meal.strMealThumb,
                        contentDescription = meal.strMeal,
                        placeholder = ColorPainter(Color.LightGray),
                        error = ColorPainter(Color.LightGray),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth().height(250.dp),
                    )
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Category: ${meal.strCategory}",
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Area: ${meal.strArea}",
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Instructions",
                            style = MaterialTheme.typography.titleLarge,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = meal.strInstructions,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                    // FAB と重ならないよう余白を確保
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}
