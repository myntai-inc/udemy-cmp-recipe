package jp.myntai.udemy.recipe.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import jp.myntai.udemy.recipe.ui.component.CategoryCard

@Composable
fun CategoryListScreen(
    onCategoryClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dummyCategories = listOf(
        "Beef", "Chicken", "Dessert", "Lamb",
        "Pasta", "Seafood", "Vegetarian", "Breakfast",
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .fillMaxSize(),
        contentPadding = PaddingValues(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(dummyCategories) { category ->
            CategoryCard(
                categoryName = category,
                categoryImageUrl = "",
                onClick = { onCategoryClick(category) },
            )
        }
    }
}
