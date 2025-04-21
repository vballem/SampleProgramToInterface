package com.example.unittestsdemo.amiibo.with_mocks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.unittestsdemo.amiibo.GetAmiibosUseCase
import com.example.unittestsdemo.amiibo.Amiibo
import com.example.unittestsdemo.amiibo.AmiiboIntent
import com.example.unittestsdemo.amiibo.AmiiboState
import com.example.unittestsdemo.amiibo.AmiiboViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AmiiboViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private lateinit var viewModel: AmiiboViewModel
    private val getAmiibosUseCase: GetAmiibosUseCase = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = AmiiboViewModel(getAmiibosUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Loading`() = testScope.runTest {
        assertEquals(AmiiboState.Loading, viewModel.state.value)
    }

    @Test
    fun `fetchAmiibos updates state to Success when use case returns data`() = testScope.runTest {
        // Given
        val amiiboList = listOf(
            Amiibo(name = "Mario", image = "url1"),
            Amiibo(name = "Luigi", image = "url2")
        )
        coEvery { getAmiibosUseCase("Mario") } returns flow { emit(amiiboList) }

        // When
        viewModel.sendIntent(AmiiboIntent.FetchAmiibos("Mario"))
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(AmiiboState.Success(amiiboList), viewModel.state.value)
        coVerify { getAmiibosUseCase("Mario") }
    }

    @Test
    fun `fetchAmiibos updates state to Error when use case throws exception`() = testScope.runTest {
        // Given
        coEvery { getAmiibosUseCase("Mario") } returns flow { throw Exception("Error") }

        // When
        viewModel.sendIntent(AmiiboIntent.FetchAmiibos("Mario"))
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(AmiiboState.Error, viewModel.state.value)
        coVerify { getAmiibosUseCase("Mario") }
    }
}