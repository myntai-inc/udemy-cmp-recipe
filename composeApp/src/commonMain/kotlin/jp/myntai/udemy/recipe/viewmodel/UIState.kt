package jp.myntai.udemy.recipe.viewmodel

sealed interface UIState<out T> {
    data object Loading : UIState<Nothing>
    data class Success<T>(val data: T) : UIState<T>
    data class Error(val message: String) : UIState<Nothing>
}
