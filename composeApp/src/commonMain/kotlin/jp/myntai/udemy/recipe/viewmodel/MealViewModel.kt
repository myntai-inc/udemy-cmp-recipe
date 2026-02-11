package jp.myntai.udemy.recipe.viewmodel

import androidx.lifecycle.ViewModel
import jp.myntai.udemy.recipe.data.model.Category
import jp.myntai.udemy.recipe.data.model.Meal
import jp.myntai.udemy.recipe.data.model.MealDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MealViewModel : ViewModel() {

    private val _categoriesState = MutableStateFlow<UIState<List<Category>>>(UIState.Loading)
    val categoriesState: StateFlow<UIState<List<Category>>> = _categoriesState.asStateFlow()

    private val _mealsState = MutableStateFlow<UIState<List<Meal>>>(UIState.Loading)
    val mealsState: StateFlow<UIState<List<Meal>>> = _mealsState.asStateFlow()

    private val _mealDetailState = MutableStateFlow<UIState<MealDetail>>(UIState.Loading)
    val mealDetailState: StateFlow<UIState<MealDetail>> = _mealDetailState.asStateFlow()

    init {
        loadCategories()
    }

    fun loadCategories() {
        _categoriesState.value = UIState.Success(
            listOf(
                Category("1", "Beef", "https://www.themealdb.com/images/category/beef.png", ""),
                Category("2", "Chicken", "https://www.themealdb.com/images/category/chicken.png", ""),
                Category("3", "Dessert", "https://www.themealdb.com/images/category/dessert.png", ""),
                Category("4", "Lamb", "https://www.themealdb.com/images/category/lamb.png", ""),
                Category("5", "Pasta", "https://www.themealdb.com/images/category/pasta.png", ""),
                Category("6", "Seafood", "https://www.themealdb.com/images/category/seafood.png", ""),
                Category("7", "Vegetarian", "https://www.themealdb.com/images/category/vegetarian.png", ""),
                Category("8", "Breakfast", "https://www.themealdb.com/images/category/breakfast.png", ""),
            )
        )
    }

    fun loadMealsByCategory(category: String) {
        _mealsState.value = UIState.Loading
        _mealsState.value = UIState.Success(
            listOf(
                Meal("52874", "Beef and Mustard Pie", "https://www.themealdb.com/images/media/meals/sytuqu1511553755.jpg"),
                Meal("52878", "Beef and Oyster pie", "https://www.themealdb.com/images/media/meals/wrssvt1511556563.jpg"),
                Meal("52997", "Beef Banh Mi Bowls", "https://www.themealdb.com/images/media/meals/z0ageb1583189517.jpg"),
                Meal("52873", "Beef Dumpling Stew", "https://www.themealdb.com/images/media/meals/uyqrrv1511553350.jpg"),
                Meal("52952", "Beef Lo Mein", "https://www.themealdb.com/images/media/meals/1529444830.jpg"),
                Meal("52824", "Beef Sunday Roast", "https://www.themealdb.com/images/media/meals/ssrrrs1503664277.jpg"),
            )
        )
    }

    fun loadMealDetail(idMeal: String) {
        _mealDetailState.value = UIState.Loading
        _mealDetailState.value = UIState.Success(
            MealDetail(
                idMeal = "52874",
                strMeal = "Beef and Mustard Pie",
                strCategory = "Beef",
                strArea = "British",
                strInstructions = "Preheat the oven to 150C/300F/Gas 2.\nToss the beef and flour together in a bowl with some salt and black pepper.",
                strMealThumb = "https://www.themealdb.com/images/media/meals/sytuqu1511553755.jpg",
            )
        )
    }
}
