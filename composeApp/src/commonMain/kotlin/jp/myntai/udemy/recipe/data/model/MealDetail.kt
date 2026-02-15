package jp.myntai.udemy.recipe.data.model

import kotlinx.serialization.Serializable

@Serializable
data class MealDetail(
    val idMeal: String,
    val strMeal: String,
    val strCategory: String,
    val strArea: String,
    val strInstructions: String,
    val strMealThumb: String,
)
