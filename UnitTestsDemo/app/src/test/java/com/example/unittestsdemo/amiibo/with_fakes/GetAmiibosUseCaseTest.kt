package com.example.unittestsdemo.amiibo.with_fakes

import com.example.unittestsdemo.amiibo.AmiibosRepository
import com.example.unittestsdemo.amiibo.GetAmiibosUseCase
import com.example.unittestsdemo.amiibo.GetAmiibosUseCaseImpl
import com.example.unittestsdemo.amiibo.Amiibo
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetAmiibosUseCaseTest {

    private lateinit var useCase: GetAmiibosUseCase
    private lateinit var fakeRepository: FakeAmiibosRepository

    @Before
    fun setUp() {
        fakeRepository = FakeAmiibosRepository()
        useCase = GetAmiibosUseCaseImpl(fakeRepository)
    }

    @Test
    fun `invoke returns list of amiibos when repository provides data`() = runTest {
        // Given
        val amiiboList = listOf(
            Amiibo(name = "Mario", image = "url1"), Amiibo(name = "Luigi", image = "url2")
        )
        fakeRepository.setAmiiboList(amiiboList)

        // When
        val result = useCase("Mario")

        // Then
        result.collect { amiibos ->
            assertEquals(amiiboList, amiibos)
        }
    }

    @Test
    fun `invoke throws exception when repository throws exception`() = runTest {
        // Given
        val exception = Exception("Error")
        fakeRepository.setShouldThrowError(true)

        // When & Assert
        try {
            useCase("Mario").collect {}
        } catch (e: Exception) {
            assertEquals(exception.message, e.message)
        }
    }
}

class FakeAmiibosRepository : AmiibosRepository {
    private var amiiboList: List<Amiibo> = emptyList()
    private var shouldThrowError: Boolean = false

    fun setAmiiboList(amiibos: List<Amiibo>) {
        amiiboList = amiibos
    }

    fun setShouldThrowError(shouldThrow: Boolean) {
        shouldThrowError = shouldThrow
    }

    override fun getAmiibos(name: String) = flow {
        if (shouldThrowError) {
            throw Exception("Error")
        } else {
            emit(amiiboList)
        }
    }
}
