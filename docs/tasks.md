# MyRecipe タスクリスト

コースのセクション構成に対応した段階的タスクリスト。
各タスクには依存関係を明示し、順番に実装を進める。

---

## Section 2: Compose Multiplatform — UI構築

UIコンポーネントと画面をダミーデータで構築する。

### Task 2.1: CategoryCard コンポーネント作成

- [x] `ui/component/CategoryCard.kt` を作成
- カテゴリ画像とカテゴリ名を表示する `Card` コンポーザブル
- `onClick` コールバックを受け取る
- **依存**: なし

### Task 2.2: MealListItem コンポーネント作成

- [x] `ui/component/MealListItem.kt` を作成
- レシピ画像とレシピ名を表示するリストアイテム
- `onClick` コールバックを受け取る
- **依存**: なし

### Task 2.3: CategoryListScreen 作成 (ダミーデータ)

- [x] `ui/screen/CategoryListScreen.kt` を作成
- `LazyVerticalGrid` (2列) で CategoryCard を表示
- ダミーのカテゴリデータを使用
- **依存**: Task 2.1

### Task 2.4: MealListScreen 作成 (ダミーデータ)

- [x] `ui/screen/MealListScreen.kt` を作成
- `LazyColumn` で MealListItem を表示
- ダミーのレシピデータを使用
- **依存**: Task 2.2

### Task 2.5: Coil3 導入と AsyncImage 対応

- [x] `libs.versions.toml` に Coil3 依存を追加
- [x] `build.gradle.kts` に Coil3 依存を追加
- [x] CategoryCard / MealListItem の画像表示を `AsyncImage` に変更
- **依存**: Task 2.1, Task 2.2

---

## Section 3: ViewModel KMP — 状態管理

ViewModel で UI 状態を管理する。

### Task 3.1: データモデル定義

- [x] `data/model/Category.kt` を作成
- [x] `data/model/Meal.kt` を作成
- [x] `data/model/MealDetail.kt` を作成
- **依存**: なし

### Task 3.2: UIState sealed interface 定義

- [x] `viewmodel/UIState.kt` を作成
- `Loading` / `Success<T>` / `Error` の3状態を定義
- **依存**: なし

### Task 3.3: MealViewModel 作成

- [x] `viewmodel/MealViewModel.kt` を作成
- `StateFlow<UIState<List<Category>>>` でカテゴリ一覧を管理
- `StateFlow<UIState<List<Meal>>>` でレシピ一覧を管理
- `StateFlow<UIState<MealDetail>>` でレシピ詳細を管理
- ダミーデータで初期化
- **依存**: Task 3.1, Task 3.2

### Task 3.4: UI と ViewModel の接続

- [x] CategoryListScreen で `collectAsStateWithLifecycle()` を使用
- [x] MealListScreen で `collectAsStateWithLifecycle()` を使用
- [x] Loading / Success / Error 状態に応じた UI 表示を実装
- **依存**: Task 2.3, Task 2.4, Task 3.3

---

## Section 4: Room KMP — ローカルDB

お気に入り機能のためのローカルデータベースを構築する。

### Task 4.1: Room KMP + KSP 依存追加

- [x] `libs.versions.toml` に Room KMP / KSP のバージョンとライブラリを追加
- [x] `build.gradle.kts` に Room / KSP プラグインと依存を追加
- **依存**: なし

### Task 4.2: FavoriteMeal Entity 定義

- [x] `data/model/FavoriteMeal.kt` を作成
- `@Entity(tableName = "favorite_meals")` アノテーション付き
- `idMeal` を `@PrimaryKey` に設定
- **依存**: Task 4.1

### Task 4.3: FavoriteMealDao 作成

- [x] `data/local/FavoriteMealDao.kt` を作成
- `@Insert(onConflict = OnConflictStrategy.REPLACE)` で追加
- `@Delete` で削除
- `@Query` で全件取得 (`Flow<List<FavoriteMeal>>`)
- `@Query` で ID 指定取得 (お気に入り状態チェック用)
- **依存**: Task 4.2

### Task 4.4: AppDatabase 定義

- [x] `data/local/AppDatabase.kt` を作成
- `@Database(entities = [FavoriteMeal::class], version = 1)`
- `abstract fun favoriteMealDao(): FavoriteMealDao`
- **依存**: Task 4.3

### Task 4.5: expect/actual で DatabaseBuilder 実装

- [x] `commonMain` に `expect fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>`
- [x] `androidMain` に `actual` 実装 (applicationContext 使用)
- [x] `iosMain` に `actual` 実装 (NSHomeDirectory 使用)
- [x] `jvmMain` に `actual` 実装 (user.home 使用)
- **依存**: Task 4.4

---

## Section 5: Koin DI — 依存注入

Koin による依存注入を導入し、各層の依存関係を管理する。

### Task 5.1: Koin 依存追加

- [x] `libs.versions.toml` に Koin のバージョンとライブラリを追加
- [x] `build.gradle.kts` に Koin 依存を追加
- **依存**: なし

### Task 5.2: appModule 定義

- [x] `di/AppModule.kt` を作成
- `single { AppDatabase }` — データベースインスタンス
- `single { FavoriteMealDao }` — DAO
- `single { MealRepository }` — リポジトリ
- `viewModel { MealViewModel }` — ViewModel
- **依存**: Task 5.1, Task 4.5

### Task 5.3: startKoin 初期化

- [x] `commonMain` で共通の Koin モジュール定義
- [x] `androidMain` の `MainActivity` / `Application` で `startKoin` 呼び出し
- [x] `iosMain` の `MainViewController` で `startKoin` 呼び出し
- [x] `jvmMain` の `main()` で `startKoin` 呼び出し
- **依存**: Task 5.2

### Task 5.4: koinViewModel() で ViewModel 注入

- [x] 各画面の ViewModel 取得を `koinViewModel()` に変更
- [x] 手動インスタンス生成を削除
- **依存**: Task 5.3, Task 3.4

---

## Section 6: Ktor Client — API連携

TheMealDB API との通信を実装し、ダミーデータから実データに切り替える。

### Task 6.1: Ktor Client + kotlinx.serialization 依存追加

- [x] `libs.versions.toml` に Ktor / kotlinx.serialization のバージョンとライブラリを追加
- [x] `build.gradle.kts` に Ktor Client 依存を追加 (各プラットフォーム用エンジン含む)
- [x] `build.gradle.kts` に kotlinx.serialization プラグインを追加
- **依存**: なし

### Task 6.2: API レスポンス用データクラス作成

- [x] 既存データモデルに `@Serializable` アノテーションを追加
- [x] `CategoryResponse` / `MealResponse` / `MealDetailResponse` ラッパークラスを作成
- **依存**: Task 6.1, Task 3.1

### Task 6.3: RemoteDataSource 実装

- [x] `data/remote/RemoteDataSource.kt` を作成
- [x] `HttpClient` の設定 (ContentNegotiation + Json)
- [x] `getCategories()` — カテゴリ一覧取得
- [x] `getMealsByCategory(category: String)` — カテゴリ別レシピ取得
- [x] `getMealDetail(idMeal: String)` — レシピ詳細取得
- **依存**: Task 6.2

### Task 6.4: MealRepository 実装

- [x] `repository/MealRepository.kt` を作成
- [x] Remote (API) と Local (Room) のデータソースを統合
- [x] カテゴリ・レシピ取得は RemoteDataSource から
- [x] お気に入り操作は FavoriteMealDao から
- **依存**: Task 6.3, Task 4.3

### Task 6.5: ダミーデータ → API データへの切り替え

- [x] MealViewModel の各メソッドを MealRepository 経由に変更
- [x] ダミーデータを削除
- [x] エラーハンドリング (try-catch → UIState.Error) を実装
- **依存**: Task 6.4, Task 5.4

---

## Section 7: Navigation KMP — 画面遷移

Navigation KMP による画面遷移を実装し、全画面を接続する。

### Task 7.1: Navigation KMP 依存追加

- [x] `libs.versions.toml` に Navigation KMP のバージョンとライブラリを追加
- [x] `build.gradle.kts` に Navigation 依存を追加
- **依存**: なし

### Task 7.2: Screen ルート定義

- [x] `navigation/Screen.kt` を作成
- [x] `CategoryList` / `MealList(category)` / `MealDetail(idMeal)` / `Favorites` を `@Serializable` で定義
- **依存**: Task 7.1

### Task 7.3: NavHost セットアップ

- [x] `navigation/AppNavHost.kt` を作成
- [x] `NavHost` で全画面のルーティングを定義
- [x] `App.kt` を NavHost ベースに書き換え
- **依存**: Task 7.2

### Task 7.4: MealDetail 画面の実装

- [x] `ui/screen/MealDetailScreen.kt` を作成
- [x] レシピ詳細情報 (画像、名前、カテゴリ、地域、手順) を表示
- [x] お気に入りボタンを実装 (Room DB と連携)
- **依存**: Task 7.3, Task 6.5

### Task 7.5: BottomNavigationBar 実装

- [x] `ui/component/BottomNavigationBar.kt` を作成
- [x] Home (CategoryList) と Favorites の2タブ
- [x] `App.kt` の `Scaffold` に `bottomBar` として配置
- **依存**: Task 7.3

### Task 7.6: Favorites 画面接続

- [x] `ui/screen/FavoritesScreen.kt` を作成
- [x] Room DB のお気に入りレシピ一覧を表示
- [x] アイテムタップで MealDetail 画面へ遷移
- **依存**: Task 7.4, Task 7.5

---

## Section 8: 動作確認

各プラットフォームでのビルドと動作確認。

### Task 8.1: Android ビルド・動作確認

- [x] `./gradlew :composeApp:assembleDebug` でビルド成功を確認
- [ ] エミュレータまたは実機で全画面の動作確認
- [ ] API通信、お気に入り保存/削除、画面遷移を確認
- **依存**: Task 7.6

### Task 8.2: iOS ビルド・動作確認

- [ ] Xcode で `iosApp` プロジェクトをビルド
- [ ] シミュレータまたは実機で全画面の動作確認
- [ ] API通信、お気に入り保存/削除、画面遷移を確認
- **依存**: Task 7.6

### Task 8.3: Desktop ビルド・動作確認

- [x] `./gradlew :composeApp:run` で Desktop アプリを起動
- [ ] 全画面の動作確認
- [ ] API通信、お気に入り保存/削除、画面遷移を確認
- **依存**: Task 7.6

---

## Section 9: リファクタリングと品質改善

コードレビューで特定した設計・品質上の問題を修正する。

### Task 9.1: 未使用テンプレートファイルの削除

- [x] `Greeting.kt`, `Platform.kt`, `Platform.android.kt`, `Platform.ios.kt`, `Platform.jvm.kt` を削除
- [x] `docs/tasks.md` に Section 9 全体のタスク定義を追記
- **依存**: なし

### Task 9.2: LazyList に key パラメータを追加

- [x] `CategoryListScreen.kt` の `items()` に `key = { it.idCategory }` を追加
- [x] `MealListScreen.kt` の `items()` に `key = { it.idMeal }` を追加
- **依存**: なし

### Task 9.3: データロードをルート引数ベースに変更

- [x] `AppNavHost.kt` でナビゲーションコールバックから `LaunchedEffect` ベースのデータロードに変更
- [x] `MealViewModel.kt` の `checkIsFavorite()` から不要なリセット (`_isFavoriteState.value = false`) を削除
- [x] `MealViewModel.kt` の `toggleFavorite()` を `Mutex` ベースに変更
- **依存**: なし

### Task 9.4: エラー画面にリトライボタンを追加

- [x] `ui/component/ErrorContent.kt` を新規作成
- [x] 4画面に `onRetry` パラメータを追加し `ErrorContent` を使用
- [x] `MealViewModel.kt` の `loadFavorites()` を `private` → `fun` に変更
- [x] `AppNavHost.kt` で各画面に `onRetry` ラムダを渡す
- **依存**: Task 9.3

### Task 9.5: 画面構造の統一 (TopAppBar)

- [x] `CategoryListScreen.kt` に `Scaffold` + `TopAppBar` (タイトル "MyRecipe") を追加
- [x] `MealListScreen.kt` に `Scaffold` + `TopAppBar` (タイトル = カテゴリ名 + 戻るボタン) を追加
- [x] `FavoritesScreen.kt` に `Scaffold` + `TopAppBar` (タイトル "Favorites") を追加
- [x] 各画面のコンテンツに `Modifier.padding(innerPadding)` を適用
- **依存**: Task 9.4

---

## Section 10: バグ修正と ViewModel 分割

MealViewModel のバグ修正と画面ごとの ViewModel 分割。

### Task 10.1: LazyList の key パラメータ追加

- [x] `CategoryListScreen.kt` の `items()` に `key = { it.idCategory }` を追加
- [x] `MealListScreen.kt` の `items()` に `key = { it.idMeal }` を追加
- [x] `docs/tasks.md` に Section 10 全体のタスク定義を追記
- **依存**: なし

### Task 10.2: loadFavorites() の重複 collect 修正

- [x] `MealViewModel.kt` の `_favoritesState` + `loadFavorites()` を `stateIn` に置き換え
- [x] `init` ブロックから `loadFavorites()` を削除
- [x] `AppNavHost.kt` の Favorites `onRetry` を no-op に変更
- **依存**: なし

### Task 10.3: isFavorite を favorites Flow から導出

- [x] `_isFavoriteState`, `checkIsFavorite()`, `Mutex` を削除
- [x] `combine` で `isFavoriteState` を導出
- [x] `toggleFavorite()` から手動トグルを削除
- [x] `AppNavHost.kt` で `checkIsFavorite()` → `setCurrentMealId()` に変更
- **依存**: Task 10.2

### Task 10.4: HttpClient の onClose 追加

- [x] `AppModule.kt` の `single<HttpClient>` に `onClose { it?.close() }` を追加
- **依存**: なし

### Task 10.5: ユーザーフレンドリーなエラーメッセージ

- [x] `viewmodel/ErrorMessages.kt` を新規作成
- [x] `Exception.toUserFriendlyMessage()` 拡張関数を実装
- [x] `MealViewModel.kt` の全エラーメッセージを置換
- **依存**: なし

### Task 10.6: ViewModel を画面ごとに分割

- [x] `CategoryListViewModel.kt` を新規作成
- [x] `MealListViewModel.kt` を新規作成
- [x] `MealDetailViewModel.kt` を新規作成
- [x] `FavoritesViewModel.kt` を新規作成
- [x] `AppModule.kt` の DI 定義を4つの ViewModel に分割
- [x] `AppNavHost.kt` で各画面内で `koinViewModel()` を取得
- [x] `App.kt` から ViewModel パラメータを削除
- [x] `MealViewModel.kt` を削除
- **依存**: Task 10.2, Task 10.3, Task 10.4, Task 10.5

### Task 10.7: docs/tasks.md の最終更新

- [x] Section 9, 10 の全チェックボックスを `[x]` に更新
- [x] 依存関係サマリーに Section 10 を追加
- **依存**: Task 10.6

---

## Section 11: 設計改善と機能追加

コードレビューで特定した設計・品質・機能面の改善。

### Task 11.1: デッドコード削除

- [x] `MealRepository.isFavorite()` を削除 (Task 10.3 で未使用になった)
- [x] `FavoriteMealDao.getById()` を削除 (isFavorite 削除に伴い未使用)
- **依存**: なし

### Task 11.2: URL パラメータエンコード

- [x] `RemoteDataSource.kt` の API 呼び出しを Ktor の `parameter()` に変更
- [x] 文字列連結による URL 組み立てを `url { }` ビルダーに置き換え
- **依存**: なし

### Task 11.3: 画像プレースホルダー追加

- [x] `CategoryCard` / `MealListItem` / `MealDetailScreen` の `AsyncImage` に `placeholder` と `error` を追加
- [x] `ColorPainter(Color.LightGray)` で全プラットフォーム対応
- **依存**: なし

### Task 11.4: FAB とコンテンツの重なり修正

- [ ] `MealDetailScreen` のスクロールコンテンツ末尾に FAB 分の `Spacer` を追加
- **依存**: なし

### Task 11.5: toggleFavorite() エラーハンドリング追加

- [ ] `MealDetailViewModel.toggleFavorite()` に try-catch を追加
- [ ] DB 操作失敗時にユーザーへ通知する仕組みを追加 (Snackbar 等)
- **依存**: なし

### Task 11.6: FavoritesScreen の onRetry 整理

- [ ] `FavoritesViewModel` は Flow ベースのため Error 状態にならない前提を明確化
- [ ] `FavoritesScreen` から不要な `onRetry` パラメータを削除、または Error 表示自体を除去
- **依存**: なし

### Task 11.7: Scaffold 構造の統一

- [ ] `App.kt` の Scaffold (bottomBar) と各画面の Scaffold (TopAppBar) のネストを解消
- [ ] 各画面から個別の Scaffold を除去し、TopAppBar を `App.kt` 側で統一管理
- [ ] 画面ごとのタイトル・ナビゲーションアイコンを NavBackStackEntry から動的に決定
- **依存**: Task 11.4, Task 11.6

### Task 11.8: テーマとダークモード対応

- [ ] `ui/theme/` パッケージにカスタムテーマを作成
- [ ] `isSystemInDarkTheme()` でライト/ダーク切り替え
- [ ] カスタムカラースキームを定義
- **依存**: なし

### Task 11.9: 材料データ (Ingredients) の表示

- [ ] `MealDetail` モデルに `strIngredient1`〜`strIngredient20` / `strMeasure1`〜`strMeasure20` を追加
- [ ] 材料と分量をペアにしてリスト化するロジックを実装
- [ ] `MealDetailScreen` に材料セクションを追加
- **依存**: なし

### Task 11.10: ViewModel のユニットテスト追加

- [ ] テスト用の依存 (kotlinx-coroutines-test, Turbine 等) を追加
- [ ] `CategoryListViewModel` のテストを作成 (Loading → Success / Error)
- [ ] `MealDetailViewModel` のテストを作成 (お気に入りトグル)
- [ ] `FakeRepository` を作成してテスト用に差し替え
- **依存**: Task 11.1〜11.9 完了後

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
    ↓
Section 9 (リファクタリング) ← コードレビュー後の品質改善
    ↓
Section 10 (バグ修正と ViewModel 分割) ← Section 9 完了後
    ↓
Section 11 (設計改善と機能追加) ← Section 10 完了後
```
