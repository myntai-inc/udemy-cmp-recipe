package jp.myntai.udemy.recipe.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import jp.myntai.udemy.recipe.data.local.AppDatabase
import jp.myntai.udemy.recipe.data.local.FavoriteMealDao
import jp.myntai.udemy.recipe.data.local.getDatabaseBuilder
import jp.myntai.udemy.recipe.data.remote.RemoteDataSource
import jp.myntai.udemy.recipe.repository.MealRepository
import jp.myntai.udemy.recipe.viewmodel.MealViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.koin.dsl.onClose

val appModule = module {
    single<HttpClient> {
        HttpClient {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 30_000
                connectTimeoutMillis = 15_000
            }
        }
    } onClose { it?.close() }
    single<AppDatabase> {
        getDatabaseBuilder()
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
    single<FavoriteMealDao> { get<AppDatabase>().favoriteMealDao() }
    single<RemoteDataSource> { RemoteDataSource(get()) }
    single<MealRepository> { MealRepository(get(), get()) }
    viewModel { MealViewModel(get()) }
}
