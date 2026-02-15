package jp.myntai.udemy.recipe.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import jp.myntai.udemy.recipe.data.model.Category
import jp.myntai.udemy.recipe.ui.component.CategoryCard
import jp.myntai.udemy.recipe.viewmodel.UIState

@Composable
fun CategoryListScreen(
    uiState: UIState<List<Category>>,
    onCategoryClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        is UIState.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }
        is UIState.Error -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = uiState.message)
            }
        }
        is UIState.Success -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = modifier
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .fillMaxSize(),
                contentPadding = PaddingValues(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(uiState.data, key = { it.idCategory }) { category ->
                    CategoryCard(
                        categoryName = category.strCategory,
                        categoryImageUrl = category.strCategoryThumb,
                        onClick = { onCategoryClick(category.strCategory) },
                    )
                }
            }
        }
    }
}
