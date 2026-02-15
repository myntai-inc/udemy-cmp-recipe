package jp.myntai.udemy.recipe.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.myntai.udemy.recipe.data.model.Category
import jp.myntai.udemy.recipe.repository.MealRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryListViewModel(private val repository: MealRepository) : ViewModel() {

    private val _categoriesState = MutableStateFlow<UIState<List<Category>>>(UIState.Loading)
    val categoriesState: StateFlow<UIState<List<Category>>> = _categoriesState.asStateFlow()

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
                _categoriesState.value = UIState.Error(e.toUserFriendlyMessage())
            }
        }
    }
}
