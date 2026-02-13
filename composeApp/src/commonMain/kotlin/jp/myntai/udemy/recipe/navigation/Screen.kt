package jp.myntai.udemy.recipe.navigation

import kotlinx.serialization.Serializable

@Serializable
data object CategoryList

@Serializable
data class MealList(val category: String)

@Serializable
data class MealDetail(val idMeal: String)

@Serializable
data object Favorites
