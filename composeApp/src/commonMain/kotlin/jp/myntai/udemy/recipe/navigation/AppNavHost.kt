package jp.myntai.udemy.recipe.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import jp.myntai.udemy.recipe.ui.screen.CategoryListScreen
import jp.myntai.udemy.recipe.ui.screen.FavoritesScreen
import jp.myntai.udemy.recipe.ui.screen.MealDetailScreen
import jp.myntai.udemy.recipe.ui.screen.MealListScreen
import jp.myntai.udemy.recipe.viewmodel.MealViewModel
import jp.myntai.udemy.recipe.viewmodel.UIState

@Composable
fun AppNavHost(
    navController: NavHostController,
    viewModel: MealViewModel,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = CategoryList,
        modifier = modifier,
    ) {
        composable<CategoryList> {
            val categoriesState = viewModel.categoriesState.collectAsStateWithLifecycle()
            CategoryListScreen(
                uiState = categoriesState.value,
                onCategoryClick = { category ->
                    navController.navigate(MealList(category))
                },
                onRetry = { viewModel.loadCategories() },
            )
        }

        composable<MealList> { backStackEntry ->
            val route = backStackEntry.toRoute<MealList>()
            LaunchedEffect(route.category) {
                viewModel.loadMealsByCategory(route.category)
            }
            val mealsState = viewModel.mealsState.collectAsStateWithLifecycle()
            MealListScreen(
                uiState = mealsState.value,
                title = route.category,
                onMealClick = { idMeal ->
                    navController.navigate(MealDetail(idMeal))
                },
                onBackClick = { navController.popBackStack() },
                onRetry = { viewModel.loadMealsByCategory(route.category) },
            )
        }

        composable<MealDetail> { backStackEntry ->
            val route = backStackEntry.toRoute<MealDetail>()
            LaunchedEffect(route.idMeal) {
                viewModel.loadMealDetail(route.idMeal)
                viewModel.checkIsFavorite(route.idMeal)
            }
            val mealDetailState = viewModel.mealDetailState.collectAsStateWithLifecycle()
            val isFavorite = viewModel.isFavoriteState.collectAsStateWithLifecycle()
            MealDetailScreen(
                uiState = mealDetailState.value,
                isFavorite = isFavorite.value,
                onFavoriteClick = {
                    val state = mealDetailState.value
                    if (state is UIState.Success) {
                        viewModel.toggleFavorite(state.data)
                    }
                },
                onBackClick = { navController.popBackStack() },
                onRetry = { viewModel.loadMealDetail(route.idMeal) },
            )
        }

        composable<Favorites> {
            val favoritesState = viewModel.favoritesState.collectAsStateWithLifecycle()
            FavoritesScreen(
                uiState = favoritesState.value,
                onMealClick = { idMeal ->
                    navController.navigate(MealDetail(idMeal))
                },
                onRetry = { },
            )
        }
    }
}
