package jp.myntai.udemy.recipe.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.myntai.udemy.recipe.data.model.FavoriteMeal
import jp.myntai.udemy.recipe.repository.MealRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class FavoritesViewModel(private val repository: MealRepository) : ViewModel() {

    val favoritesState: StateFlow<UIState<List<FavoriteMeal>>> =
        repository.getFavorites()
            .map<List<FavoriteMeal>, UIState<List<FavoriteMeal>>> { UIState.Success(it) }
            .catch { emit(UIState.Error((it as? Exception)?.toUserFriendlyMessage() ?: "An unexpected error occurred.")) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = UIState.Loading,
            )
}
