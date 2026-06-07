package jp.myntai.udemy.recipe

import androidx.compose.ui.window.ComposeUIViewController
import jp.myntai.udemy.recipe.di.initKoin

fun MainViewController() = run {
    initKoin()
    ComposeUIViewController { App() }
}
