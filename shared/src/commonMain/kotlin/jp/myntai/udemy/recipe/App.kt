package jp.myntai.udemy.recipe

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import jp.myntai.udemy.recipe.navigation.AppNavHost
import jp.myntai.udemy.recipe.navigation.MealDetail
import jp.myntai.udemy.recipe.ui.component.BottomNavigationBar

@Composable
fun App() {
    val navController = rememberNavController()

    MaterialTheme {
        Scaffold(
            bottomBar = {
                val currentDestination =
                    navController.currentBackStackEntryAsState().value?.destination
                if (currentDestination?.hasRoute(MealDetail::class) != true) {
                    BottomNavigationBar(navController = navController)
                }
            },
        ) { innerPadding ->
            AppNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}
