package jp.myntai.udemy.recipe.di

import org.koin.core.context.startKoin
import org.koin.mp.KoinPlatform

fun initKoin() {
    if (KoinPlatform.getKoinOrNull() != null) return
    startKoin {
        modules(appModule)
    }
}
