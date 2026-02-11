package jp.myntai.udemy.recipe

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import jp.myntai.udemy.recipe.ui.screen.CategoryListScreen

@Composable
fun App() {
    MaterialTheme {
        CategoryListScreen(
            onCategoryClick = {},
        )
    }
}
