package jp.myntai.udemy.recipe.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.myntai.udemy.recipe.data.model.Category
import jp.myntai.udemy.recipe.data.model.Meal
import jp.myntai.udemy.recipe.data.model.MealDetail
import jp.myntai.udemy.recipe.repository.MealRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MealViewModel(private val repository: MealRepository) : ViewModel() {

    private val _categoriesState = MutableStateFlow<UIState<List<Category>>>(UIState.Loading)
    val categoriesState: StateFlow<UIState<List<Category>>> = _categoriesState.asStateFlow()

    private val _mealsState = MutableStateFlow<UIState<List<Meal>>>(UIState.Loading)
    val mealsState: StateFlow<UIState<List<Meal>>> = _mealsState.asStateFlow()

    private val _mealDetailState = MutableStateFlow<UIState<MealDetail>>(UIState.Loading)
    val mealDetailState: StateFlow<UIState<MealDetail>> = _mealDetailState.asStateFlow()

    init {
        loadCategories()
    }

    fun loadCategories() {
        viewModelScope.launch {
            _categoriesState.value = UIState.Loading
            try {
                val categories = repository.getCategories()
                _categoriesState.value = UIState.Success(categories)
            } catch (e: Exception) {
                _categoriesState.value = UIState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun loadMealsByCategory(category: String) {
        viewModelScope.launch {
            _mealsState.value = UIState.Loading
            try {
                val meals = repository.getMealsByCategory(category)
                _mealsState.value = UIState.Success(meals)
            } catch (e: Exception) {
                _mealsState.value = UIState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun loadMealDetail(idMeal: String) {
        viewModelScope.launch {
            _mealDetailState.value = UIState.Loading
            try {
                val detail = repository.getMealDetail(idMeal)
                _mealDetailState.value = UIState.Success(detail)
            } catch (e: Exception) {
                _mealDetailState.value = UIState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
