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

## Build Commands

- Android debug build: `./gradlew :composeApp:assembleDebug`
- Desktop run: `./gradlew :composeApp:run`
- Compile check (Desktop): `./gradlew :composeApp:compileKotlinDesktop`

## Package Structure

```
composeApp/src/commonMain/kotlin/jp/myntai/udemy/recipe/
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
| Coil3 | 3.3.0 |
| Room KMP | 2.8.4 |
| KSP | 2.3.5 |
| SQLite Bundled | 2.6.2 |
| Koin | 4.1.1 |
| Ktor | 3.4.0 |
| kotlinx.serialization | 1.10.0 |
| Navigation Compose | 2.9.1 |
