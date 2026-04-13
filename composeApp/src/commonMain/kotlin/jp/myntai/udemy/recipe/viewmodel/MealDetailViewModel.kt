package jp.myntai.udemy.recipe.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.myntai.udemy.recipe.data.model.FavoriteMeal
import jp.myntai.udemy.recipe.data.model.MealDetail
import jp.myntai.udemy.recipe.repository.MealRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MealDetailViewModel(private val repository: MealRepository) : ViewModel() {

    private val _mealDetailState = MutableStateFlow<UIState<MealDetail>>(UIState.Loading)
    val mealDetailState: StateFlow<UIState<MealDetail>> = _mealDetailState.asStateFlow()

    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    private val _currentMealId = MutableStateFlow<String?>(null)

    val isFavoriteState: StateFlow<Boolean> =
        combine(repository.getFavorites(), _currentMealId) { favorites, currentMealId ->
            favorites.any { it.idMeal == currentMealId }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    fun messageShown() {
        _userMessage.value = null
    }

    fun setCurrentMealId(idMeal: String) {
        _currentMealId.value = idMeal
    }

    private var loadMealDetailJob: Job? = null

    fun loadMealDetail(idMeal: String) {
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
