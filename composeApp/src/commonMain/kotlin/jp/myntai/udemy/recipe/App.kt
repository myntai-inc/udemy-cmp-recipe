package jp.myntai.udemy.recipe

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import jp.myntai.udemy.recipe.ui.screen.CategoryListScreen
import jp.myntai.udemy.recipe.viewmodel.MealViewModel

@Composable
fun App() {
    val viewModel = viewModel { MealViewModel() }
    val categoriesState = viewModel.categoriesState.collectAsStateWithLifecycle()

    MaterialTheme {
        CategoryListScreen(
            uiState = categoriesState.value,
            onCategoryClick = {},
        )
    }
}
