package jp.myntai.udemy.recipe.ui.component

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import cmprecipe.shared.generated.resources.Res
import cmprecipe.shared.generated.resources.ic_favorite
import cmprecipe.shared.generated.resources.ic_home
import jp.myntai.udemy.recipe.navigation.CategoryList
import jp.myntai.udemy.recipe.navigation.Favorites
import jp.myntai.udemy.recipe.navigation.MealList
import org.jetbrains.compose.resources.vectorResource

@Composable
fun BottomNavigationBar(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentDestination = currentBackStackEntry?.destination

    NavigationBar(modifier = modifier) {
        NavigationBarItem(
            selected = currentDestination?.hasRoute(CategoryList::class) == true
                || currentDestination?.hasRoute(MealList::class) == true,
            onClick = {
                navController.navigate(CategoryList) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = { Icon(vectorResource(Res.drawable.ic_home), contentDescription = "Home") },
            label = { Text("Home") },
        )
        NavigationBarItem(
            selected = currentDestination?.hasRoute(Favorites::class) == true,
            onClick = {
                navController.navigate(Favorites) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = { Icon(vectorResource(Res.drawable.ic_favorite), contentDescription = "Favorites") },
            label = { Text("Favorites") },
        )
    }
}
