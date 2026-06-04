package com.example.laba5.viewmodel

import android.app.Application
import com.example.laba5.data.UserPreferences
import com.example.laba5.data.local.AppDatabase
import com.example.laba5.model.Category
import com.example.laba5.model.Transaction
import com.example.laba5.model.Type
import com.example.laba5.model.UiState
import com.example.laba5.repository.TransactionRepository
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.unmockkAll
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ListViewModelTest {

    private lateinit var viewModel: ListViewModel
    private val repository: TransactionRepository = mockk(relaxed = true)
    private val app: Application = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockkConstructor(AppDatabase::class)
        mockkConstructor(UserPreferences::class)

        viewModel = ListViewModel(app)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun testLoadSuccessfullyFetchesData() = runTest {
        // Arrange
        val mockList = listOf(Transaction(1, 100.0, Type.EXPENSE, Category.FOOD, false, null))
        coEvery { repository.getAll() } returns mockList

        // Act
        viewModel.load()
        advanceUntilIdle()

        // Assert
        val state = viewModel.state.value
        assertTrue(state is UiState.Success)
        assertEquals(mockList, (state as UiState.Success).data)
    }

    @Test
    fun testLoadHandlesExceptionAndReturnsError() = runTest {
        // Arrange
        coEvery { repository.getAll() } throws Exception("Network Error")

        // Act
        viewModel.load()
        advanceUntilIdle()

        // Assert
        val state = viewModel.state.value
        assertTrue(state is UiState.Error)
        assertEquals("❌ Немає інтернету", (state as UiState.Error).message)
    }

    @Test
    fun testGetTransactionByIdReturnsNullIfMissing() = runTest {
        // Arrange
        val mockList = listOf(Transaction(1, 100.0, Type.EXPENSE, Category.FOOD, false, null))
        coEvery { repository.getAll() } returns mockList
        viewModel.load()
        advanceUntilIdle()

        // Act
        val result = viewModel.getTransactionById(999)

        // Assert
        assertNull(result)
    }
}