package jp.myntai.udemy.recipe.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import jp.myntai.udemy.recipe.data.model.Category
import jp.myntai.udemy.recipe.data.model.CategoryResponse
import jp.myntai.udemy.recipe.data.model.Meal
import jp.myntai.udemy.recipe.data.model.MealDetail
import jp.myntai.udemy.recipe.data.model.MealDetailResponse
import jp.myntai.udemy.recipe.data.model.MealResponse
import kotlinx.serialization.json.Json

class RemoteDataSource {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    suspend fun getCategories(): List<Category> {
        val response: CategoryResponse = client.get("$BASE_URL/categories.php").body()
        return response.categories
    }

    suspend fun getMealsByCategory(category: String): List<Meal> {
        val response: MealResponse = client.get("$BASE_URL/filter.php?c=$category").body()
        return response.meals.orEmpty()
    }

    suspend fun getMealDetail(idMeal: String): MealDetail? {
        val response: MealDetailResponse = client.get("$BASE_URL/lookup.php?i=$idMeal").body()
        return response.meals?.firstOrNull()
    }

    companion object {
        private const val BASE_URL = "https://www.themealdb.com/api/json/v1/1"
    }
}
