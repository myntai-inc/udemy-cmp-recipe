package jp.myntai.udemy.recipe.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.myntai.udemy.recipe.data.model.Category
import jp.myntai.udemy.recipe.data.model.FavoriteMeal
import jp.myntai.udemy.recipe.data.model.Meal
import jp.myntai.udemy.recipe.data.model.MealDetail
import jp.myntai.udemy.recipe.repository.MealRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class MealViewModel(private val repository: MealRepository) : ViewModel() {

    private val _categoriesState = MutableStateFlow<UIState<List<Category>>>(UIState.Loading)
    val categoriesState: StateFlow<UIState<List<Category>>> = _categoriesState.asStateFlow()

    private val _mealsState = MutableStateFlow<UIState<List<Meal>>>(UIState.Loading)
    val mealsState: StateFlow<UIState<List<Meal>>> = _mealsState.asStateFlow()

    private val _mealDetailState = MutableStateFlow<UIState<MealDetail>>(UIState.Loading)
    val mealDetailState: StateFlow<UIState<MealDetail>> = _mealDetailState.asStateFlow()

    private val _favoritesState = MutableStateFlow<UIState<List<FavoriteMeal>>>(UIState.Loading)
    val favoritesState: StateFlow<UIState<List<FavoriteMeal>>> = _favoritesState.asStateFlow()

    private val _isFavoriteState = MutableStateFlow(false)
    val isFavoriteState: StateFlow<Boolean> = _isFavoriteState.asStateFlow()

    init {
        loadCategories()
        loadFavorites()
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

    private var loadMealsJob: Job? = null

    fun loadMealsByCategory(category: String) {
        loadMealsJob?.cancel()
        loadMealsJob = viewModelScope.launch {
            _mealsState.value = UIState.Loading
            try {
                val meals = repository.getMealsByCategory(category)
                _mealsState.value = UIState.Success(meals)
            } catch (e: Exception) {
                _mealsState.value = UIState.Error(e.message ?: "Unknown error")
            }
        }
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
            } catch (e: Exception) {
                _mealDetailState.value = UIState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            repository.getFavorites().collect { favorites ->
                _favoritesState.value = UIState.Success(favorites)
            }
        }
    }

    fun checkIsFavorite(idMeal: String) {
        viewModelScope.launch {
            toggleFavoriteMutex.withLock {
                _isFavoriteState.value = repository.isFavorite(idMeal)
            }
        }
    }

    private val toggleFavoriteMutex = Mutex()

    fun toggleFavorite(mealDetail: MealDetail) {
        viewModelScope.launch {
            toggleFavoriteMutex.withLock {
                val favorite = FavoriteMeal(
                    idMeal = mealDetail.idMeal,
                    strMeal = mealDetail.strMeal,
                    strMealThumb = mealDetail.strMealThumb,
                )
                if (_isFavoriteState.value) {
                    repository.removeFavorite(favorite)
                    _isFavoriteState.value = false
                } else {
                    repository.addFavorite(favorite)
                    _isFavoriteState.value = true
                }
            }
        }
    }
}
