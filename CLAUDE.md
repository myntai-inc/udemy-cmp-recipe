# CLAUDE.md

## Project Overview

MyRecipe - Compose Multiplatform recipe app using TheMealDB API.
Targets: Android, iOS, Desktop (JVM).

## Task Management

- Tasks are defined in `docs/tasks.md` and implemented one at a time
- Each task = 1 PR
- Branch naming: `feat/task-{N.M}-{short-description}` (e.g., `feat/task-2.4-meal-list-screen`)
- Commit message: `feat: {summary}` format
- After each PR creation, wait for user confirmation before proceeding to the next task

## Module Structure

Multi-module layout (matches the latest Kotlin Multiplatform wizard):

- `shared/` — KMP library with all shared code (Android via
  `com.android.kotlin.multiplatform.library`, iOS, JVM). Holds common/iOS/JVM/Android
  source sets, Room + KSP, namespace `jp.myntai.udemy.recipe.shared`.
- `androidApp/` — `com.android.application`. MainActivity, manifest, launcher resources.
- `desktopApp/` — `org.jetbrains.kotlin.jvm`. Compose Desktop entry point (`main.kt`).
- `iosApp/` — Xcode project; builds the `ComposeApp` framework from `:shared`.

## Build Commands

- Android debug build: `./gradlew :androidApp:assembleDebug`
- Desktop run: `./gradlew :desktopApp:run`
- Compile check (Desktop/common): `./gradlew :shared:compileKotlinJvm`
- Run unit tests: `./gradlew :shared:jvmTest`
- iOS framework (called by Xcode): `./gradlew :shared:embedAndSignAppleFrameworkForXcode`

## Package Structure

```
shared/src/commonMain/kotlin/jp/myntai/udemy/recipe/
  ├── data/
  │   ├── model/        # Data models (Category, Meal, MealDetail, FavoriteMeal)
  │   ├── local/        # Room DB (AppDatabase, FavoriteMealDao)
  │   └── remote/       # Ktor API (RemoteDataSource)
  ├── di/               # Koin modules
  ├── navigation/       # Navigation routes
  ├── repository/       # MealRepository
  ├── viewmodel/        # MealViewModel, UIState
  └── ui/
      ├── component/    # Reusable composables (CategoryCard, MealListItem, BottomNavigationBar)
      └── screen/       # Screen composables (CategoryList, MealList, MealDetail, Favorites)
```

## Library Versions

| Library | Version |
|---------|---------|
| AGP | 9.2.1 |
| Gradle | 9.5.1 |
| Kotlin | 2.4.0 |
| KSP | 2.3.9 |
| Compose Multiplatform | 1.11.1 |
| Coil3 | 3.4.0 |
| Room KMP | 2.8.4 |
| SQLite Bundled | 2.6.2 |
| Koin | 4.2.1 |
| Ktor | 3.5.0 |
| kotlinx.serialization | 1.11.0 |
| Navigation Compose | 2.9.2 |
