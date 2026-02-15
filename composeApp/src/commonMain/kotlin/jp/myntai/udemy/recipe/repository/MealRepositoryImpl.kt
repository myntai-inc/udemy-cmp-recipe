package jp.myntai.udemy.recipe.repository

import jp.myntai.udemy.recipe.data.local.FavoriteMealDao
import jp.myntai.udemy.recipe.data.model.Category
import jp.myntai.udemy.recipe.data.model.FavoriteMeal
import jp.myntai.udemy.recipe.data.model.Meal
import jp.myntai.udemy.recipe.data.model.MealDetail
import jp.myntai.udemy.recipe.data.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow

class MealRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val favoriteMealDao: FavoriteMealDao,
) : MealRepository {
    override suspend fun getCategories(): List<Category> =
        remoteDataSource.getCategories()

    override suspend fun getMealsByCategory(category: String): List<Meal> =
        remoteDataSource.getMealsByCategory(category)

    override suspend fun getMealDetail(idMeal: String): MealDetail? =
        remoteDataSource.getMealDetail(idMeal)

    override fun getFavorites(): Flow<List<FavoriteMeal>> =
        favoriteMealDao.getAll()

    override suspend fun addFavorite(meal: FavoriteMeal) =
        favoriteMealDao.insert(meal)

    override suspend fun removeFavorite(meal: FavoriteMeal) =
        favoriteMealDao.delete(meal)
}
