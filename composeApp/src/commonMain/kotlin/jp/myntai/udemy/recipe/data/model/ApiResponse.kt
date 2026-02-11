package jp.myntai.udemy.recipe.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CategoryResponse(val categories: List<Category>)

@Serializable
data class MealResponse(val meals: List<Meal>)

@Serializable
data class MealDetailResponse(val meals: List<MealDetail>)
