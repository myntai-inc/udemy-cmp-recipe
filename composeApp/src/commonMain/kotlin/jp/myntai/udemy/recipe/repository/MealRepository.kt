package jp.myntai.udemy.recipe.repository

import jp.myntai.udemy.recipe.data.model.Category
import jp.myntai.udemy.recipe.data.model.FavoriteMeal
import jp.myntai.udemy.recipe.data.model.Meal
import jp.myntai.udemy.recipe.data.model.MealDetail
import kotlinx.coroutines.flow.Flow

interface MealRepository {
    suspend fun getCategories(): List<Category>
    suspend fun getMealsByCategory(category: String): List<Meal>
    suspend fun getMealDetail(idMeal: String): MealDetail?
    fun getFavorites(): Flow<List<FavoriteMeal>>
    suspend fun addFavorite(meal: FavoriteMeal)
    suspend fun removeFavorite(meal: FavoriteMeal)
}
