# MyRecipe タスクリスト

コースのセクション構成に対応した段階的タスクリスト。
各タスクには依存関係を明示し、順番に実装を進める。

---

## Section 2: Compose Multiplatform — UI構築

UIコンポーネントと画面をダミーデータで構築する。

### Task 2.1: CategoryCard コンポーネント作成

- [ ] `ui/component/CategoryCard.kt` を作成
- カテゴリ画像とカテゴリ名を表示する `Card` コンポーザブル
- `onClick` コールバックを受け取る
- **依存**: なし

### Task 2.2: MealListItem コンポーネント作成

- [ ] `ui/component/MealListItem.kt` を作成
- レシピ画像とレシピ名を表示するリストアイテム
- `onClick` コールバックを受け取る
- **依存**: なし

### Task 2.3: CategoryListScreen 作成 (ダミーデータ)

- [ ] `ui/screen/CategoryListScreen.kt` を作成
- `LazyVerticalGrid` (2列) で CategoryCard を表示
- ダミーのカテゴリデータを使用
- **依存**: Task 2.1

### Task 2.4: MealListScreen 作成 (ダミーデータ)

- [ ] `ui/screen/MealListScreen.kt` を作成
- `LazyColumn` で MealListItem を表示
- ダミーのレシピデータを使用
- **依存**: Task 2.2

### Task 2.5: Coil3 導入と AsyncImage 対応

- [ ] `libs.versions.toml` に Coil3 依存を追加
- [ ] `build.gradle.kts` に Coil3 依存を追加
- [ ] CategoryCard / MealListItem の画像表示を `AsyncImage` に変更
- **依存**: Task 2.1, Task 2.2

---

## Section 3: ViewModel KMP — 状態管理

ViewModel で UI 状態を管理する。

### Task 3.1: データモデル定義

- [ ] `data/model/Category.kt` を作成
- [ ] `data/model/Meal.kt` を作成
- [ ] `data/model/MealDetail.kt` を作成
- **依存**: なし

### Task 3.2: UIState sealed interface 定義

- [ ] `viewmodel/UIState.kt` を作成
- `Loading` / `Success<T>` / `Error` の3状態を定義
- **依存**: なし

### Task 3.3: MealViewModel 作成

- [ ] `viewmodel/MealViewModel.kt` を作成
- `StateFlow<UIState<List<Category>>>` でカテゴリ一覧を管理
- `StateFlow<UIState<List<Meal>>>` でレシピ一覧を管理
- `StateFlow<UIState<MealDetail>>` でレシピ詳細を管理
- ダミーデータで初期化
- **依存**: Task 3.1, Task 3.2

### Task 3.4: UI と ViewModel の接続

- [ ] CategoryListScreen で `collectAsStateWithLifecycle()` を使用
- [ ] MealListScreen で `collectAsStateWithLifecycle()` を使用
- [ ] Loading / Success / Error 状態に応じた UI 表示を実装
- **依存**: Task 2.3, Task 2.4, Task 3.3

---

## Section 4: Room KMP — ローカルDB

お気に入り機能のためのローカルデータベースを構築する。

### Task 4.1: Room KMP + KSP 依存追加

- [ ] `libs.versions.toml` に Room KMP / KSP のバージョンとライブラリを追加
- [ ] `build.gradle.kts` に Room / KSP プラグインと依存を追加
- **依存**: なし

### Task 4.2: FavoriteMeal Entity 定義

- [ ] `data/model/FavoriteMeal.kt` を作成
- `@Entity(tableName = "favorite_meals")` アノテーション付き
- `idMeal` を `@PrimaryKey` に設定
- **依存**: Task 4.1

### Task 4.3: FavoriteMealDao 作成

- [ ] `data/local/FavoriteMealDao.kt` を作成
- `@Insert(onConflict = OnConflictStrategy.REPLACE)` で追加
- `@Delete` で削除
- `@Query` で全件取得 (`Flow<List<FavoriteMeal>>`)
- `@Query` で ID 指定取得 (お気に入り状態チェック用)
- **依存**: Task 4.2

### Task 4.4: AppDatabase 定義

- [ ] `data/local/AppDatabase.kt` を作成
- `@Database(entities = [FavoriteMeal::class], version = 1)`
- `abstract fun favoriteMealDao(): FavoriteMealDao`
- **依存**: Task 4.3

### Task 4.5: expect/actual で DatabaseBuilder 実装

- [ ] `commonMain` に `expect fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>`
- [ ] `androidMain` に `actual` 実装 (applicationContext 使用)
- [ ] `iosMain` に `actual` 実装 (NSHomeDirectory 使用)
- [ ] `jvmMain` に `actual` 実装 (user.home 使用)
- **依存**: Task 4.4

---

## Section 5: Koin DI — 依存注入

Koin による依存注入を導入し、各層の依存関係を管理する。

### Task 5.1: Koin 依存追加

- [ ] `libs.versions.toml` に Koin のバージョンとライブラリを追加
- [ ] `build.gradle.kts` に Koin 依存を追加
- **依存**: なし

### Task 5.2: appModule 定義

- [ ] `di/AppModule.kt` を作成
- `single { AppDatabase }` — データベースインスタンス
- `single { FavoriteMealDao }` — DAO
- `single { MealRepository }` — リポジトリ
- `viewModel { MealViewModel }` — ViewModel
- **依存**: Task 5.1, Task 4.5

### Task 5.3: startKoin 初期化

- [ ] `commonMain` で共通の Koin モジュール定義
- [ ] `androidMain` の `MainActivity` / `Application` で `startKoin` 呼び出し
- [ ] `iosMain` の `MainViewController` で `startKoin` 呼び出し
- [ ] `jvmMain` の `main()` で `startKoin` 呼び出し
- **依存**: Task 5.2

### Task 5.4: koinViewModel() で ViewModel 注入

- [ ] 各画面の ViewModel 取得を `koinViewModel()` に変更
- [ ] 手動インスタンス生成を削除
- **依存**: Task 5.3, Task 3.4

---

## Section 6: Ktor Client — API連携

TheMealDB API との通信を実装し、ダミーデータから実データに切り替える。

### Task 6.1: Ktor Client + kotlinx.serialization 依存追加

- [ ] `libs.versions.toml` に Ktor / kotlinx.serialization のバージョンとライブラリを追加
- [ ] `build.gradle.kts` に Ktor Client 依存を追加 (各プラットフォーム用エンジン含む)
- [ ] `build.gradle.kts` に kotlinx.serialization プラグインを追加
- **依存**: なし

### Task 6.2: API レスポンス用データクラス作成

- [ ] 既存データモデルに `@Serializable` アノテーションを追加
- [ ] `CategoryResponse` / `MealResponse` / `MealDetailResponse` ラッパークラスを作成
- **依存**: Task 6.1, Task 3.1

### Task 6.3: RemoteDataSource 実装

- [ ] `data/remote/RemoteDataSource.kt` を作成
- [ ] `HttpClient` の設定 (ContentNegotiation + Json)
- [ ] `getCategories()` — カテゴリ一覧取得
- [ ] `getMealsByCategory(category: String)` — カテゴリ別レシピ取得
- [ ] `getMealDetail(idMeal: String)` — レシピ詳細取得
- **依存**: Task 6.2

### Task 6.4: MealRepository 実装

- [ ] `repository/MealRepository.kt` を作成
- [ ] Remote (API) と Local (Room) のデータソースを統合
- [ ] カテゴリ・レシピ取得は RemoteDataSource から
- [ ] お気に入り操作は FavoriteMealDao から
- **依存**: Task 6.3, Task 4.3

### Task 6.5: ダミーデータ → API データへの切り替え

- [ ] MealViewModel の各メソッドを MealRepository 経由に変更
- [ ] ダミーデータを削除
- [ ] エラーハンドリング (try-catch → UIState.Error) を実装
- **依存**: Task 6.4, Task 5.4

---

## Section 7: Navigation KMP — 画面遷移

Navigation KMP による画面遷移を実装し、全画面を接続する。

### Task 7.1: Navigation KMP 依存追加

- [ ] `libs.versions.toml` に Navigation KMP のバージョンとライブラリを追加
- [ ] `build.gradle.kts` に Navigation 依存を追加
- **依存**: なし

### Task 7.2: Screen ルート定義

- [ ] `navigation/Screen.kt` を作成
- [ ] `CategoryList` / `MealList(category)` / `MealDetail(idMeal)` / `Favorites` を `@Serializable` で定義
- **依存**: Task 7.1

### Task 7.3: NavHost セットアップ

- [ ] `navigation/AppNavHost.kt` を作成
- [ ] `NavHost` で全画面のルーティングを定義
- [ ] `App.kt` を NavHost ベースに書き換え
- **依存**: Task 7.2

### Task 7.4: MealDetail 画面の実装

- [ ] `ui/screen/MealDetailScreen.kt` を作成
- [ ] レシピ詳細情報 (画像、名前、カテゴリ、地域、手順) を表示
- [ ] お気に入りボタンを実装 (Room DB と連携)
- **依存**: Task 7.3, Task 6.5

### Task 7.5: BottomNavigationBar 実装

- [ ] `ui/component/BottomNavigationBar.kt` を作成
- [ ] Home (CategoryList) と Favorites の2タブ
- [ ] `App.kt` の `Scaffold` に `bottomBar` として配置
- **依存**: Task 7.3

### Task 7.6: Favorites 画面接続

- [ ] `ui/screen/FavoritesScreen.kt` を作成
- [ ] Room DB のお気に入りレシピ一覧を表示
- [ ] アイテムタップで MealDetail 画面へ遷移
- **依存**: Task 7.4, Task 7.5

---

## Section 8: 動作確認

各プラットフォームでのビルドと動作確認。

### Task 8.1: Android ビルド・動作確認

- [ ] `./gradlew :composeApp:assembleDebug` でビルド成功を確認
- [ ] エミュレータまたは実機で全画面の動作確認
- [ ] API通信、お気に入り保存/削除、画面遷移を確認
- **依存**: Task 7.6

### Task 8.2: iOS ビルド・動作確認

- [ ] Xcode で `iosApp` プロジェクトをビルド
- [ ] シミュレータまたは実機で全画面の動作確認
- [ ] API通信、お気に入り保存/削除、画面遷移を確認
- **依存**: Task 7.6

### Task 8.3: Desktop ビルド・動作確認

- [ ] `./gradlew :composeApp:run` で Desktop アプリを起動
- [ ] 全画面の動作確認
- [ ] API通信、お気に入り保存/削除、画面遷移を確認
- **依存**: Task 7.6

---

## 依存関係サマリー

```
Section 2 (UI構築)
    ↓
Section 3 (ViewModel) ← Section 2 の画面が必要
    ↓
Section 4 (Room DB)   ← 並行して作業可能
    ↓
Section 5 (Koin DI)   ← Section 3, 4 の成果物を注入
    ↓
Section 6 (Ktor API)  ← Section 5 で DI 基盤が整っていること
    ↓
Section 7 (Navigation) ← Section 6 で全データ層が完成していること
    ↓
Section 8 (動作確認)   ← 全セクション完了後
```
