package jp.myntai.udemy.recipe.repository

import jp.myntai.udemy.recipe.data.local.FavoriteMealDao
import jp.myntai.udemy.recipe.data.model.Category
import jp.myntai.udemy.recipe.data.model.FavoriteMeal
import jp.myntai.udemy.recipe.data.model.Meal
import jp.myntai.udemy.recipe.data.model.MealDetail
import jp.myntai.udemy.recipe.data.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow

class MealRepository(
    private val remoteDataSource: RemoteDataSource,
    private val favoriteMealDao: FavoriteMealDao,
) {
    suspend fun getCategories(): List<Category> =
        remoteDataSource.getCategories()

    suspend fun getMealsByCategory(category: String): List<Meal> =
        remoteDataSource.getMealsByCategory(category)

    suspend fun getMealDetail(idMeal: String): MealDetail? =
        remoteDataSource.getMealDetail(idMeal)

    fun getFavorites(): Flow<List<FavoriteMeal>> =
        favoriteMealDao.getAll()

    suspend fun addFavorite(meal: FavoriteMeal) =
        favoriteMealDao.insert(meal)

    suspend fun removeFavorite(meal: FavoriteMeal) =
        favoriteMealDao.delete(meal)

    suspend fun isFavorite(idMeal: String): Boolean =
        favoriteMealDao.getById(idMeal) != null
}
