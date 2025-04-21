package com.example.unittestsdemo.amiibo.with_mocks

import com.example.unittestsdemo.amiibo.AmiibosNetworkDataSource
import com.example.unittestsdemo.amiibo.AmiibosRepository
import com.example.unittestsdemo.amiibo.AmiibosRepositoryImpl
import com.example.unittestsdemo.amiibo.Amiibo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AmiibosRepositoryImplTest {

    private lateinit var repository: AmiibosRepository
    private val dataSource: AmiibosNetworkDataSource = mockk()
    private val dispatcher: CoroutineDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(dispatcher)

    @Before
    fun setUp() {
        repository = AmiibosRepositoryImpl(dataSource, dispatcher)
    }

    @Test
    fun `getAmiibos returns list of amiibos when dataSource provides data`() = testScope.runTest {
        // Given
        val amiiboList = listOf(
            Amiibo(name = "Mario", image = "url1"),
            Amiibo(name = "Luigi", image = "url2")
        )
        coEvery { dataSource.getAmiibos("Mario") } returns amiiboList

        // When
        val result = repository.getAmiibos("Mario")

        // Then
        result.collect { amiibos ->
            assertEquals(amiiboList, amiibos)
        }
        coVerify { dataSource.getAmiibos("Mario") }
    }

    @Test
    fun `getAmiibos throws exception when dataSource throws exception`() = testScope.runTest {
        // Given
        val exception = Exception("Error")
        coEvery { dataSource.getAmiibos("Mario") } throws exception

        // When & Assert
        try {
            repository.getAmiibos("Mario").collect {}
        } catch (e: Exception) {
            assertEquals(exception, e)
        }
        coVerify { dataSource.getAmiibos("Mario") }
    }
}