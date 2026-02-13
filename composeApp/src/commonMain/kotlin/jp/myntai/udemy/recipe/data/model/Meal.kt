package jp.myntai.udemy.recipe.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Meal(
    val idMeal: String,
    val strMeal: String,
    val strMealThumb: String,
)
