package jp.myntai.udemy.recipe

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import jp.myntai.udemy.recipe.di.initKoin

fun main() {
    initKoin()
    application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "CmpRecipe",
    ) {
        App()
    }
    }
}