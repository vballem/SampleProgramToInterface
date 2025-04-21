package com.example.unittestsdemo.amiibo.with_mocks

import com.example.unittestsdemo.amiibo.AmiibosRepository
import com.example.unittestsdemo.amiibo.GetAmiibosUseCase
import com.example.unittestsdemo.amiibo.GetAmiibosUseCaseImpl
import com.example.unittestsdemo.amiibo.Amiibo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetAmiibosUseCaseTest {

    private lateinit var useCase: GetAmiibosUseCase
    private val repository: AmiibosRepository = mockk()
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setUp() {
        useCase = GetAmiibosUseCaseImpl(repository)
    }

    @Test
    fun `invoke returns list of amiibos when repository provides data`() = testScope.runTest {
        // Given
        val amiiboList = listOf(
            Amiibo(name = "Mario", image = "url1"),
            Amiibo(name = "Luigi", image = "url2")
        )
        coEvery { repository.getAmiibos("Mario") } returns flow { emit(amiiboList) }

        // When
        val result = useCase("Mario")

        // Then
        result.collect { amiibos ->
            assertEquals(amiiboList, amiibos)
        }
        coVerify { repository.getAmiibos("Mario") }
    }

    @Test
    fun `invoke throws exception when repository throws exception`() = testScope.runTest {
        // Given
        val exception = Exception("Error")
        coEvery { repository.getAmiibos("Mario") } returns flow { throw exception }

        // When & Assert
        try {
            useCase("Mario").collect {}
        } catch (e: Exception) {
            assertEquals(exception, e)
        }
        coVerify { repository.getAmiibos("Mario") }
    }
}