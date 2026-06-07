package jp.myntai.udemy.recipe.viewmodel

import app.cash.turbine.test
import jp.myntai.udemy.recipe.data.model.MealDetail
import jp.myntai.udemy.recipe.repository.FakeMealRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.advanceUntilIdle
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
    private lateinit var viewModel: MealDetailViewModel

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
        viewModel = MealDetailViewModel(repository)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadMealDetail emits Loading then Success`() = runTest {
        repository.mealDetailToReturn = testMealDetail

        viewModel.mealDetailState.test {
            assertIs<UIState.Loading>(awaitItem())

            viewModel.loadMealDetail("123")

            val success = awaitItem()
            assertIs<UIState.Success<MealDetail>>(success)
            assertEquals(testMealDetail, success.data)
        }
    }

    @Test
    fun `loadMealDetail emits Error when null returned`() = runTest {
        repository.mealDetailToReturn = null

        viewModel.mealDetailState.test {
            assertIs<UIState.Loading>(awaitItem())

            viewModel.loadMealDetail("999")

            val error = awaitItem()
            assertIs<UIState.Error>(error)
            assertEquals("Meal not found", error.message)
        }
    }

    @Test
    fun `loadMealDetail emits Error when exception thrown`() = runTest {
        repository.exceptionToThrow = RuntimeException("Network error")

        viewModel.mealDetailState.test {
            assertIs<UIState.Loading>(awaitItem())

            viewModel.loadMealDetail("123")

            val error = awaitItem()
            assertIs<UIState.Error>(error)
        }
    }

    @Test
    fun `toggleFavorite adds favorite when not favorited`() = runTest {
        repository.mealDetailToReturn = testMealDetail
        viewModel.setCurrentMealId("123")

        viewModel.isFavoriteState.test {
            assertEquals(false, awaitItem())

            viewModel.toggleFavorite(testMealDetail)

            assertEquals(true, awaitItem())
        }
    }

    @Test
    fun `toggleFavorite removes favorite when already favorited`() = runTest {
        repository.mealDetailToReturn = testMealDetail
        viewModel.setCurrentMealId("123")

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
        repository.mealDetailToReturn = testMealDetail
        viewModel.setCurrentMealId("123")
        repository.addFavoriteException = RuntimeException("DB error")

        assertNull(viewModel.userMessage.value)
        viewModel.toggleFavorite(testMealDetail)
        advanceUntilIdle()
        assertEquals("Failed to update favorite. Please try again.", viewModel.userMessage.value)

        viewModel.messageShown()
        assertNull(viewModel.userMessage.value)
    }
}
