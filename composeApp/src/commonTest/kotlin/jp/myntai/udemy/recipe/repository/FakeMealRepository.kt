package jp.myntai.udemy.recipe.repository

import jp.myntai.udemy.recipe.data.model.Category
import jp.myntai.udemy.recipe.data.model.FavoriteMeal
import jp.myntai.udemy.recipe.data.model.Meal
import jp.myntai.udemy.recipe.data.model.MealDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeMealRepository : MealRepository {

    var categoriesToReturn: List<Category> = emptyList()
    var mealsToReturn: List<Meal> = emptyList()
    var mealDetailToReturn: MealDetail? = null

    var exceptionToThrow: Exception? = null
    var addFavoriteException: Exception? = null
    var removeFavoriteException: Exception? = null

    private val _favorites = MutableStateFlow<List<FavoriteMeal>>(emptyList())

    override suspend fun getCategories(): List<Category> {
        exceptionToThrow?.let { throw it }
        return categoriesToReturn
    }

    override suspend fun getMealsByCategory(category: String): List<Meal> {
        exceptionToThrow?.let { throw it }
        return mealsToReturn
    }

    override suspend fun getMealDetail(idMeal: String): MealDetail? {
        exceptionToThrow?.let { throw it }
        return mealDetailToReturn
    }

    override fun getFavorites(): Flow<List<FavoriteMeal>> = _favorites.asStateFlow()

    override suspend fun addFavorite(meal: FavoriteMeal) {
        addFavoriteException?.let { throw it }
        _favorites.value = _favorites.value + meal
    }

    override suspend fun removeFavorite(meal: FavoriteMeal) {
        removeFavoriteException?.let { throw it }
        _favorites.value = _favorites.value.filter { it.idMeal != meal.idMeal }
    }
}
