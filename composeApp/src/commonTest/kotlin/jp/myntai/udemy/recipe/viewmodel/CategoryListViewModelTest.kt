package jp.myntai.udemy.recipe.viewmodel

import app.cash.turbine.test
import jp.myntai.udemy.recipe.data.model.Category
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
class CategoryListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: FakeMealRepository

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeMealRepository()
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init emits Loading then Success when categories load successfully`() = runTest {
        val categories = listOf(
            Category("1", "Beef", "https://example.com/beef.png", "Beef dishes"),
            Category("2", "Chicken", "https://example.com/chicken.png", "Chicken dishes"),
        )
        repository.categoriesToReturn = categories

        val viewModel = CategoryListViewModel(repository)

        viewModel.categoriesState.test {
            assertIs<UIState.Loading>(awaitItem())
            val success = awaitItem()
            assertIs<UIState.Success<List<Category>>>(success)
            assertEquals(categories, success.data)
        }
    }

    @Test
    fun `init emits Loading then Error when API fails`() = runTest {
        repository.exceptionToThrow = RuntimeException("Network error")

        val viewModel = CategoryListViewModel(repository)

        viewModel.categoriesState.test {
            assertIs<UIState.Loading>(awaitItem())
            val error = awaitItem()
            assertIs<UIState.Error>(error)
        }
    }

    @Test
    fun `loadCategories retries from Error to Success`() = runTest {
        repository.exceptionToThrow = RuntimeException("Network error")

        val viewModel = CategoryListViewModel(repository)

        viewModel.categoriesState.test {
            assertIs<UIState.Loading>(awaitItem())
            assertIs<UIState.Error>(awaitItem())

            val categories = listOf(
                Category("1", "Beef", "https://example.com/beef.png", "Beef dishes"),
            )
            repository.exceptionToThrow = null
            repository.categoriesToReturn = categories

            viewModel.loadCategories()

            assertIs<UIState.Loading>(awaitItem())
            val success = awaitItem()
            assertIs<UIState.Success<List<Category>>>(success)
            assertEquals(categories, success.data)
        }
    }
}
