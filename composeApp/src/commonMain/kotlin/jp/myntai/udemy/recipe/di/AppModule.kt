package jp.myntai.udemy.recipe.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import jp.myntai.udemy.recipe.data.local.AppDatabase
import jp.myntai.udemy.recipe.data.local.FavoriteMealDao
import jp.myntai.udemy.recipe.data.local.getDatabaseBuilder
import jp.myntai.udemy.recipe.data.remote.RemoteDataSource
import jp.myntai.udemy.recipe.repository.MealRepository
import jp.myntai.udemy.recipe.viewmodel.MealViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<AppDatabase> {
        getDatabaseBuilder()
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
    single<FavoriteMealDao> { get<AppDatabase>().favoriteMealDao() }
    single<RemoteDataSource> { RemoteDataSource() }
    single<MealRepository> { MealRepository(get(), get()) }
    viewModel { MealViewModel(get()) }
}
