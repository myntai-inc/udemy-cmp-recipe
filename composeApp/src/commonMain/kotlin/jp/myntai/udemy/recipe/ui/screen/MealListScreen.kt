package jp.myntai.udemy.recipe.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import jp.myntai.udemy.recipe.ui.component.MealListItem

@Composable
fun MealListScreen(
    onMealClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dummyMeals = listOf(
        "Beef and Mustard Pie" to "https://www.themealdb.com/images/media/meals/sytuqu1511553755.jpg",
        "Beef and Oyster pie" to "https://www.themealdb.com/images/media/meals/wrssvt1511556563.jpg",
        "Beef Banh Mi Bowls" to "https://www.themealdb.com/images/media/meals/z0ageb1583189517.jpg",
        "Beef Dumpling Stew" to "https://www.themealdb.com/images/media/meals/uyqrrv1511553350.jpg",
        "Beef Lo Mein" to "https://www.themealdb.com/images/media/meals/1529444830.jpg",
        "Beef Sunday Roast" to "https://www.themealdb.com/images/media/meals/ssrrrs1503664277.jpg",
        "Beef Wellington" to "https://www.themealdb.com/images/media/meals/vvpprx1487325699.jpg",
        "Bistek" to "https://www.themealdb.com/images/media/meals/4pqimk1683207418.jpg",
    )

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(dummyMeals) { (name, imageUrl) ->
            MealListItem(
                mealName = name,
                mealImageUrl = imageUrl,
                onClick = { onMealClick(name) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
