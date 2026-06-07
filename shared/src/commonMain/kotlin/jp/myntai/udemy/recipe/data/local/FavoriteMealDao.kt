package jp.myntai.udemy.recipe.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import jp.myntai.udemy.recipe.data.model.FavoriteMeal
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteMealDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(meal: FavoriteMeal)

    @Delete
    suspend fun delete(meal: FavoriteMeal)

    @Query("SELECT * FROM favorite_meals")
    fun getAll(): Flow<List<FavoriteMeal>>
}
