package jp.myntai.udemy.recipe.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import jp.myntai.udemy.recipe.data.model.FavoriteMeal
import jp.myntai.udemy.recipe.data.model.MealDetail
import jp.myntai.udemy.recipe.navigation.MealDetail as MealDetailRoute
import jp.myntai.udemy.recipe.repository.MealRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MealDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: MealRepository,
) : ViewModel() {

    // The destination owns its input: read the typed nav route from the handle
    // instead of having the NavHost push the id in through a LaunchedEffect.
    private val idMeal: String = savedStateHandle.toRoute<MealDetailRoute>().idMeal

    private val _mealDetailState = MutableStateFlow<UIState<MealDetail>>(UIState.Loading)
    val mealDetailState: StateFlow<UIState<MealDetail>> = _mealDetailState.asStateFlow()

    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    val isFavoriteState: StateFlow<Boolean> =
        repository.getFavorites()
            .map { favorites -> favorites.any { it.idMeal == idMeal } }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = false,
            )

    private var loadMealDetailJob: Job? = null

    init {
        loadMealDetail()
    }

    fun messageShown() {
        _userMessage.value = null
    }

    fun loadMealDetail() {
        loadMealDetailJob?.cancel()
        loadMealDetailJob = viewModelScope.launch {
            _mealDetailState.value = UIState.Loading
            try {
                val detail = repository.getMealDetail(idMeal)
                if (detail != null) {
                    _mealDetailState.value = UIState.Success(detail)
                } else {
                    _mealDetailState.value = UIState.Error("Meal not found")
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _mealDetailState.value = UIState.Error(e.toUserFriendlyMessage())
            }
        }
    }

    fun toggleFavorite(mealDetail: MealDetail) {
        viewModelScope.launch {
            try {
                val favorite = FavoriteMeal(
                    idMeal = mealDetail.idMeal,
                    strMeal = mealDetail.strMeal,
                    strMealThumb = mealDetail.strMealThumb,
                )
                if (isFavoriteState.value) {
                    repository.removeFavorite(favorite)
                } else {
                    repository.addFavorite(favorite)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (_: Exception) {
                _userMessage.value = "Failed to update favorite. Please try again."
            }
        }
    }
}
