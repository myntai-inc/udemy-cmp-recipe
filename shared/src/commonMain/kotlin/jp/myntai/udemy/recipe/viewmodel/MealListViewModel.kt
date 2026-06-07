package jp.myntai.udemy.recipe.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import jp.myntai.udemy.recipe.data.model.Meal
import jp.myntai.udemy.recipe.navigation.MealList
import jp.myntai.udemy.recipe.repository.MealRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MealListViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: MealRepository,
) : ViewModel() {

    val category: String = savedStateHandle.toRoute<MealList>().category

    private val _mealsState = MutableStateFlow<UIState<List<Meal>>>(UIState.Loading)
    val mealsState: StateFlow<UIState<List<Meal>>> = _mealsState.asStateFlow()

    private var loadMealsJob: Job? = null

    init {
        loadMeals()
    }

    fun loadMeals() {
        loadMealsJob?.cancel()
        loadMealsJob = viewModelScope.launch {
            _mealsState.value = UIState.Loading
            try {
                val meals = repository.getMealsByCategory(category)
                _mealsState.value = UIState.Success(meals)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _mealsState.value = UIState.Error(e.toUserFriendlyMessage())
            }
        }
    }
}
