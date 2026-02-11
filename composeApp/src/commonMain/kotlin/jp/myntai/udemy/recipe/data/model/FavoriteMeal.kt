package jp.myntai.udemy.recipe.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_meals")
data class FavoriteMeal(
    @PrimaryKey val idMeal: String,
    val strMeal: String,
    val strMealThumb: String,
)
