package jp.myntai.udemy.recipe.viewmodel

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import jp.myntai.udemy.recipe.data.model.Meal
import jp.myntai.udemy.recipe.repository.FakeMealRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@OptIn(ExperimentalCoroutinesApi::class)
class MealListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: FakeMealRepository

    private val testMeals = listOf(
        Meal(idMeal = "1", strMeal = "Beef Pie", strMealThumb = "https://example.com/1.png"),
        Meal(idMeal = "2", strMeal = "Beef Stew", strMealThumb = "https://example.com/2.png"),
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

    // The ViewModel reads its category from the SavedStateHandle (typed nav route)
    // and loads in init, mirroring how Koin/Navigation provide it at runtime.
    private fun createViewModel(category: String = "Beef") =
        MealListViewModel(SavedStateHandle(mapOf("category" to category)), repository)

    @Test
    fun `exposes category from the saved state handle`() = runTest {
        val viewModel = createViewModel("Chicken")
        assertEquals("Chicken", viewModel.category)
    }

    @Test
    fun `init emits Loading then Success`() = runTest {
        repository.mealsToReturn = testMeals
        val viewModel = createViewModel()

        viewModel.mealsState.test {
            assertIs<UIState.Loading>(awaitItem())

            val success = awaitItem()
            assertIs<UIState.Success<List<Meal>>>(success)
            assertEquals(testMeals, success.data)
        }
    }

    @Test
    fun `init emits Loading then Error when API fails`() = runTest {
        repository.exceptionToThrow = RuntimeException("Network error")
        val viewModel = createViewModel()

        viewModel.mealsState.test {
            assertIs<UIState.Loading>(awaitItem())
            assertIs<UIState.Error>(awaitItem())
        }
    }

    @Test
    fun `loadMeals retries from Error to Success`() = runTest {
        repository.exceptionToThrow = RuntimeException("Network error")
        val viewModel = createViewModel()

        viewModel.mealsState.test {
            assertIs<UIState.Loading>(awaitItem())
            assertIs<UIState.Error>(awaitItem())

            repository.exceptionToThrow = null
            repository.mealsToReturn = testMeals

            viewModel.loadMeals()

            assertIs<UIState.Loading>(awaitItem())
            val success = awaitItem()
            assertIs<UIState.Success<List<Meal>>>(success)
            assertEquals(testMeals, success.data)
        }
    }
}
