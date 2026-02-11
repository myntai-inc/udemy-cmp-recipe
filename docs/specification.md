# MyRecipe アプリ仕様書

## 1. アプリ概要

**MyRecipe** は、世界各国のレシピを探索できるマルチプラットフォーム対応アプリ。
TheMealDB API からカテゴリ・レシピ情報を取得し、お気に入り機能でローカルに保存できる。

Compose Multiplatform (CMP) を使用し、Android / iOS / Desktop で同一のコードベースから動作する。

## 2. 対象プラットフォーム

| プラットフォーム | 詳細 |
|---------------|------|
| Android | minSdk 24, targetSdk 36, compileSdk 36 |
| iOS | iosArm64, iosSimulatorArm64 |
| Desktop | JVM (macOS / Windows / Linux) |

## 3. 技術スタック

| カテゴリ | ライブラリ | 備考 |
|---------|----------|------|
| UI | Compose Multiplatform + Material3 | テンプレートに導入済み |
| 状態管理 | ViewModel KMP (Google公式) + StateFlow | テンプレートに導入済み (lifecycle-viewmodel-compose) |
| ローカルDB | Room KMP (Google公式) + KSP | お気に入り保存用 |
| ネットワーク | Ktor Client + kotlinx.serialization | TheMealDB API 通信 |
| DI | Koin | 依存注入フレームワーク |
| ナビゲーション | Navigation KMP (Google公式) | 画面遷移管理 |
| 画像読み込み | Coil3 | AsyncImage によるネットワーク画像表示 |

### 現在の依存関係 (テンプレート初期状態)

- Kotlin: 2.3.0
- Compose Multiplatform: 1.10.0
- Material3: 1.10.0-alpha05
- AndroidX Lifecycle: 2.9.6
- Kotlinx Coroutines: 1.10.2

## 4. 画面構成

アプリは以下の4画面で構成される。

### 4.1 CategoryList — カテゴリ一覧

- TheMealDB API からカテゴリ一覧を取得して表示
- 2列グリッド (`LazyVerticalGrid`) でカテゴリカードを表示
- 各カードにはカテゴリ画像 (`strCategoryThumb`) とカテゴリ名 (`strCategory`) を表示
- カードタップで MealList 画面へ遷移

### 4.2 MealList — レシピ一覧

- 選択されたカテゴリに属するレシピ一覧を表示
- 各アイテムにはレシピ画像 (`strMealThumb`) とレシピ名 (`strMeal`) を表示
- アイテムタップで MealDetail 画面へ遷移

### 4.3 MealDetail — レシピ詳細

- レシピの詳細情報を表示
- 表示項目: 画像、レシピ名、カテゴリ、地域 (strArea)、調理手順 (strInstructions)
- お気に入りボタン (FAB またはアイコン) でローカルDBに保存/削除

### 4.4 Favorites — お気に入り一覧

- Room DB に保存されたお気に入りレシピの一覧を表示
- BottomNavigation で CategoryList と切り替え可能
- アイテムタップで MealDetail 画面へ遷移

## 5. データモデル

### 5.1 API レスポンスモデル

```kotlin
@Serializable
data class CategoryResponse(
    val categories: List<Category>
)

@Serializable
data class Category(
    val idCategory: String,
    val strCategory: String,
    val strCategoryThumb: String,
    val strCategoryDescription: String
)

@Serializable
data class MealResponse(
    val meals: List<Meal>
)

@Serializable
data class Meal(
    val idMeal: String,
    val strMeal: String,
    val strMealThumb: String
)

@Serializable
data class MealDetailResponse(
    val meals: List<MealDetail>
)

@Serializable
data class MealDetail(
    val idMeal: String,
    val strMeal: String,
    val strCategory: String,
    val strArea: String,
    val strInstructions: String,
    val strMealThumb: String
)
```

### 5.2 Room Entity

```kotlin
@Entity(tableName = "favorite_meals")
data class FavoriteMeal(
    @PrimaryKey
    val idMeal: String,
    val strMeal: String,
    val strMealThumb: String
)
```

## 6. API 仕様

使用する外部API: **TheMealDB** (https://www.themealdb.com/api.php)

| エンドポイント | 用途 | URL |
|-------------|------|-----|
| カテゴリ一覧 | CategoryList 画面 | `https://www.themealdb.com/api/json/v1/1/categories.php` |
| カテゴリ別レシピ | MealList 画面 | `https://www.themealdb.com/api/json/v1/1/filter.php?c={category}` |
| レシピ詳細 | MealDetail 画面 | `https://www.themealdb.com/api/json/v1/1/lookup.php?i={idMeal}` |

すべて GET リクエスト。認証不要。レスポンスは JSON 形式。

## 7. アーキテクチャ

Clean Architecture + MVVM パターンを採用。

```
UI Layer (Composable)
  ↓ collectAsStateWithLifecycle()
ViewModel Layer (StateFlow<UIState>)
  ↓
Repository Layer (データ統合)
  ↓             ↓
RemoteDataSource   LocalDataSource
(Ktor Client)     (Room DAO)
```

### パッケージ構成

```
jp.myntai.udemy.recipe/
├── ui/
│   ├── screen/
│   │   ├── CategoryListScreen.kt
│   │   ├── MealListScreen.kt
│   │   ├── MealDetailScreen.kt
│   │   └── FavoritesScreen.kt
│   └── component/
│       ├── CategoryCard.kt
│       └── MealListItem.kt
├── viewmodel/
│   └── MealViewModel.kt
├── repository/
│   └── MealRepository.kt
├── data/
│   ├── remote/
│   │   └── RemoteDataSource.kt
│   ├── local/
│   │   ├── AppDatabase.kt
│   │   ├── FavoriteMealDao.kt
│   │   └── DatabaseBuilder.kt  (expect/actual)
│   └── model/
│       ├── Category.kt
│       ├── Meal.kt
│       ├── MealDetail.kt
│       └── FavoriteMeal.kt
├── di/
│   └── AppModule.kt
├── navigation/
│   ├── Screen.kt
│   └── AppNavHost.kt
├── App.kt
└── Platform.kt
```

## 8. UIState 設計

すべての画面で統一された sealed interface パターンを使用。

```kotlin
sealed interface UIState<out T> {
    data object Loading : UIState<Nothing>
    data class Success<T>(val data: T) : UIState<T>
    data class Error(val message: String) : UIState<Nothing>
}
```

ViewModel 内で `StateFlow<UIState<T>>` として管理し、UI 層で `collectAsStateWithLifecycle()` で購読。

## 9. ナビゲーションフロー

```
BottomNavigation
├── Home (CategoryList)
│   └── MealList (category を引数で受け取り)
│       └── MealDetail (idMeal を引数で受け取り)
└── Favorites
    └── MealDetail (idMeal を引数で受け取り)
```

### Screen ルート定義

```kotlin
@Serializable
data object CategoryList : Screen

@Serializable
data class MealList(val category: String) : Screen

@Serializable
data class MealDetail(val idMeal: String) : Screen

@Serializable
data object Favorites : Screen
```

Navigation KMP の Type-Safe Navigation を使用。画面間のデータ受け渡しは `@Serializable` なルートオブジェクトで型安全に行う。

## 10. expect/actual パターン

Room のデータベースビルダーはプラットフォーム固有の実装が必要。

```kotlin
// commonMain
expect fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>

// androidMain
actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    // applicationContext を使用して DB パスを指定
}

// iosMain
actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    // NSHomeDirectory を使用して DB パスを指定
}

// jvmMain (Desktop)
actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    // System.getProperty("user.home") を使用して DB パスを指定
}
```
