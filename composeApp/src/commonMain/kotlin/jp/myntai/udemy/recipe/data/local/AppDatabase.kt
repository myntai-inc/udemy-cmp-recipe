package jp.myntai.udemy.recipe.data.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import jp.myntai.udemy.recipe.data.model.FavoriteMeal

@Database(entities = [FavoriteMeal::class], version = 1)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteMealDao(): FavoriteMealDao
}

expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase>
