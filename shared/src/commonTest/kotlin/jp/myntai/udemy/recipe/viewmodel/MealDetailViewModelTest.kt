package jp.myntai.udemy.recipe.viewmodel

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import jp.myntai.udemy.recipe.data.model.MealDetail
import jp.myntai.udemy.recipe.repository.FakeMealRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class MealDetailViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: FakeMealRepository

    private val testMealDetail = MealDetail(
        idMeal = "123",
        strMeal = "Test Meal",
        strCategory = "Beef",
        strArea = "British",
        strInstructions = "Test instructions",
        strMealThumb = "https://example.com/meal.png",
    )

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeMealRepository()
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // The ViewModel reads its meal id from the SavedStateHandle (typed nav route)
    // and loads in init, mirroring how Koin/Navigation provide it at runtime.
    private fun createViewModel(idMeal: String = "123") =
        MealDetailViewModel(SavedStateHandle(mapOf("idMeal" to idMeal)), repository)

    @Test
    fun `emits Loading then Success`() = runTest {
        repository.mealDetailToReturn = testMealDetail
        val viewModel = createViewModel()

        viewModel.mealDetailState.test {
            assertIs<UIState.Loading>(awaitItem())

            val success = awaitItem()
            assertIs<UIState.Success<MealDetail>>(success)
            assertEquals(testMealDetail, success.data)
        }
    }

    @Test
    fun `emits Error when null returned`() = runTest {
        repository.mealDetailToReturn = null
        val viewModel = createViewModel("999")

        viewModel.mealDetailState.test {
            assertIs<UIState.Loading>(awaitItem())

            val error = awaitItem()
            assertIs<UIState.Error>(error)
            assertEquals("Meal not found", error.message)
        }
    }

    @Test
    fun `emits Error when exception thrown`() = runTest {
        repository.exceptionToThrow = RuntimeException("Network error")
        val viewModel = createViewModel()

        viewModel.mealDetailState.test {
            assertIs<UIState.Loading>(awaitItem())

            val error = awaitItem()
            assertIs<UIState.Error>(error)
        }
    }

    @Test
    fun `toggleFavorite adds favorite when not favorited`() = runTest {
        val viewModel = createViewModel()

        viewModel.isFavoriteState.test {
            assertEquals(false, awaitItem())

            viewModel.toggleFavorite(testMealDetail)

            assertEquals(true, awaitItem())
        }
    }

    @Test
    fun `toggleFavorite removes favorite when already favorited`() = runTest {
        val viewModel = createViewModel()

        viewModel.isFavoriteState.test {
            assertEquals(false, awaitItem())

            // Add favorite first
            viewModel.toggleFavorite(testMealDetail)
            assertEquals(true, awaitItem())

            // Remove favorite
            viewModel.toggleFavorite(testMealDetail)
            assertEquals(false, awaitItem())
        }
    }

    @Test
    fun `toggleFavorite sets userMessage on failure`() = runTest {
        val viewModel = createViewModel()
        repository.addFavoriteException = RuntimeException("DB error")

        assertNull(viewModel.userMessage.value)
        viewModel.toggleFavorite(testMealDetail)
        advanceUntilIdle()
        assertEquals("Failed to update favorite. Please try again.", viewModel.userMessage.value)

        viewModel.messageShown()
        assertNull(viewModel.userMessage.value)
    }
}
