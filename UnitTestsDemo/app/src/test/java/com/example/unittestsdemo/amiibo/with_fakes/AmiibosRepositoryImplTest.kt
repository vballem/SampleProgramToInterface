package com.example.unittestsdemo.amiibo.with_fakes

import com.example.unittestsdemo.amiibo.Amiibo
import com.example.unittestsdemo.amiibo.AmiibosNetworkDataSource
import com.example.unittestsdemo.amiibo.AmiibosRepository
import com.example.unittestsdemo.amiibo.AmiibosRepositoryImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AmiibosRepositoryImplTest {

    private lateinit var repository: AmiibosRepository
    private lateinit var fakeDataSource: FakeAmiibosNetworkDataSource
    private val dispatcher: CoroutineDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(dispatcher)

    @Before
    fun setUp() {
        fakeDataSource = FakeAmiibosNetworkDataSource()
        repository = AmiibosRepositoryImpl(fakeDataSource, dispatcher)
    }

    @Test
    fun `getAmiibos returns list of amiibos when dataSource provides data`() = testScope.runTest {
        // Given
        val amiiboList = listOf(
            Amiibo(name = "Mario", image = "url1"),
            Amiibo(name = "Luigi", image = "url2")
        )
        fakeDataSource.setAmiiboList(amiiboList)

        // When
        val result = repository.getAmiibos("Mario")

        // Then
        result.collect { amiibos ->
            assertEquals(amiiboList, amiibos)
        }
    }

    @Test
    fun `getAmiibos throws exception when dataSource throws exception`() = testScope.runTest {
        // Given
        val exception = Exception("Error")
        fakeDataSource.setShouldThrowError(true)

        // When & Assert
        try {
            repository.getAmiibos("Mario").collect {}
        } catch (e: Exception) {
            assertEquals(exception.message, e.message)
        }
    }
}

class FakeAmiibosNetworkDataSource : AmiibosNetworkDataSource {
    private var amiiboList: List<Amiibo> = emptyList()
    private var shouldThrowError: Boolean = false

    fun setAmiiboList(amiibos: List<Amiibo>) {
        amiiboList = amiibos
    }

    fun setShouldThrowError(shouldThrow: Boolean) {
        shouldThrowError = shouldThrow
    }

    override suspend fun getAmiibos(name: String): List<Amiibo> {
        if (shouldThrowError) {
            throw Exception("Error")
        }
        return amiiboList
    }
}