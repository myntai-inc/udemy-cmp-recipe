package jp.myntai.udemy.recipe.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import jp.myntai.udemy.recipe.data.model.Meal
import jp.myntai.udemy.recipe.ui.component.ErrorContent
import jp.myntai.udemy.recipe.ui.component.MealListItem
import jp.myntai.udemy.recipe.viewmodel.UIState

@Composable
fun MealListScreen(
    uiState: UIState<List<Meal>>,
    onMealClick: (String) -> Unit,
    onRetry: () -> Unit,
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
            ErrorContent(
                message = uiState.message,
                onRetry = onRetry,
                modifier = modifier.fillMaxSize(),
            )
        }
        is UIState.Success -> {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(uiState.data, key = { it.idMeal }) { meal ->
                    MealListItem(
                        mealName = meal.strMeal,
                        mealImageUrl = meal.strMealThumb,
                        onClick = { onMealClick(meal.idMeal) },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}
