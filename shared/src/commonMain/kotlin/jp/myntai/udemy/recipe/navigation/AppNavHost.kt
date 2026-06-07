package jp.myntai.udemy.recipe.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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
import jp.myntai.udemy.recipe.viewmodel.CategoryListViewModel
import jp.myntai.udemy.recipe.viewmodel.FavoritesViewModel
import jp.myntai.udemy.recipe.viewmodel.MealDetailViewModel
import jp.myntai.udemy.recipe.viewmodel.MealListViewModel
import jp.myntai.udemy.recipe.viewmodel.UIState
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = CategoryList,
        modifier = modifier,
    ) {
        composable<CategoryList> {
            val viewModel = koinViewModel<CategoryListViewModel>()
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
            val viewModel = koinViewModel<MealListViewModel>()
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
            val viewModel = koinViewModel<MealDetailViewModel>()
            val route = backStackEntry.toRoute<MealDetail>()
            val snackbarHostState = remember { SnackbarHostState() }
            LaunchedEffect(route.idMeal) {
                viewModel.loadMealDetail(route.idMeal)
                viewModel.setCurrentMealId(route.idMeal)
            }
            val userMessage = viewModel.userMessage.collectAsStateWithLifecycle()
            userMessage.value?.let { message ->
                LaunchedEffect(message) {
                    snackbarHostState.showSnackbar(message)
                    viewModel.messageShown()
                }
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
                snackbarHostState = snackbarHostState,
            )
        }

        composable<Favorites> {
            val viewModel = koinViewModel<FavoritesViewModel>()
            val favoritesState = viewModel.favoritesState.collectAsStateWithLifecycle()
            FavoritesScreen(
                uiState = favoritesState.value,
                onMealClick = { idMeal ->
                    navController.navigate(MealDetail(idMeal))
                },
            )
        }
    }
}
