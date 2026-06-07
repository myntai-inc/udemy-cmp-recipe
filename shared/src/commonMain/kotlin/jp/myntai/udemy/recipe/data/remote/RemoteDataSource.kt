package jp.myntai.udemy.recipe.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.URLProtocol
import io.ktor.http.path
import jp.myntai.udemy.recipe.data.model.Category
import jp.myntai.udemy.recipe.data.model.CategoryResponse
import jp.myntai.udemy.recipe.data.model.Meal
import jp.myntai.udemy.recipe.data.model.MealDetail
import jp.myntai.udemy.recipe.data.model.MealDetailResponse
import jp.myntai.udemy.recipe.data.model.MealResponse

class RemoteDataSource(private val client: HttpClient) {

    suspend fun getCategories(): List<Category> {
        val response: CategoryResponse = client.get {
            url {
                protocol = URLProtocol.HTTPS
                host = HOST
                path("$PATH_PREFIX/categories.php")
            }
        }.body()
        return response.categories
    }

    suspend fun getMealsByCategory(category: String): List<Meal> {
        val response: MealResponse = client.get {
            url {
                protocol = URLProtocol.HTTPS
                host = HOST
                path("$PATH_PREFIX/filter.php")
                parameter("c", category)
            }
        }.body()
        return response.meals.orEmpty()
    }

    suspend fun getMealDetail(idMeal: String): MealDetail? {
        val response: MealDetailResponse = client.get {
            url {
                protocol = URLProtocol.HTTPS
                host = HOST
                path("$PATH_PREFIX/lookup.php")
                parameter("i", idMeal)
            }
        }.body()
        return response.meals?.firstOrNull()
    }

    companion object {
        private const val HOST = "www.themealdb.com"
        private const val PATH_PREFIX = "api/json/v1/1"
    }
}
