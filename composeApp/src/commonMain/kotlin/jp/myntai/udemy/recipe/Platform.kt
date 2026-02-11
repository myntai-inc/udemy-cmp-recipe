package jp.myntai.udemy.recipe

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform