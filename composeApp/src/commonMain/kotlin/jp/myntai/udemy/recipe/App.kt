package jp.myntai.udemy.recipe

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jp.myntai.udemy.recipe.ui.screen.CategoryListScreen
import jp.myntai.udemy.recipe.viewmodel.MealViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App(viewModel: MealViewModel = koinViewModel()) {
    val categoriesState = viewModel.categoriesState.collectAsStateWithLifecycle()

    MaterialTheme {
        CategoryListScreen(
            uiState = categoriesState.value,
            onCategoryClick = {},
        )
    }
}
