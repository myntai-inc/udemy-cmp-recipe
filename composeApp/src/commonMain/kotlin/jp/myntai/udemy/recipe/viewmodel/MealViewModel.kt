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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MealViewModel(private val repository: MealRepository) : ViewModel() {

    private val _categoriesState = MutableStateFlow<UIState<List<Category>>>(UIState.Loading)
    val categoriesState: StateFlow<UIState<List<Category>>> = _categoriesState.asStateFlow()

    private val _mealsState = MutableStateFlow<UIState<List<Meal>>>(UIState.Loading)
    val mealsState: StateFlow<UIState<List<Meal>>> = _mealsState.asStateFlow()

    private val _mealDetailState = MutableStateFlow<UIState<MealDetail>>(UIState.Loading)
    val mealDetailState: StateFlow<UIState<MealDetail>> = _mealDetailState.asStateFlow()

    val favoritesState: StateFlow<UIState<List<FavoriteMeal>>> =
        repository.getFavorites()
            .map<List<FavoriteMeal>, UIState<List<FavoriteMeal>>> { UIState.Success(it) }
            .catch { emit(UIState.Error((it as? Exception)?.toUserFriendlyMessage() ?: "An unexpected error occurred.")) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = UIState.Loading,
            )

    private val _currentMealId = MutableStateFlow<String?>(null)

    val isFavoriteState: StateFlow<Boolean> =
        combine(repository.getFavorites(), _currentMealId) { favorites, currentMealId ->
            favorites.any { it.idMeal == currentMealId }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    init {
        loadCategories()
    }

    fun setCurrentMealId(idMeal: String) {
        _currentMealId.value = idMeal
    }

    fun loadCategories() {
        viewModelScope.launch {
            _categoriesState.value = UIState.Loading
            try {
                val categories = repository.getCategories()
                _categoriesState.value = UIState.Success(categories)
            } catch (e: Exception) {
                _categoriesState.value = UIState.Error(e.toUserFriendlyMessage())
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
                _mealsState.value = UIState.Error(e.toUserFriendlyMessage())
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
                _mealDetailState.value = UIState.Error(e.toUserFriendlyMessage())
            }
        }
    }

    fun toggleFavorite(mealDetail: MealDetail) {
        viewModelScope.launch {
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
        }
    }
}
