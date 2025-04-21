package com.example.unittestsdemo.amiibo.with_mocks

import com.example.unittestsdemo.amiibo.Amiibo
import com.example.unittestsdemo.amiibo.AmiiboService
import com.example.unittestsdemo.amiibo.Amiibos
import com.example.unittestsdemo.amiibo.AmiibosNetworkDataSource
import com.example.unittestsdemo.amiibo.AmiibosNetworkDataSourceImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class AmiibosNetworkDataSourceImplTest {

    private lateinit var subject: AmiibosNetworkDataSource

    private val amiiboServiceMock: AmiiboService = mockk(name = "mockService")

    @Before
    fun setUp() {
        subject = AmiibosNetworkDataSourceImpl(amiiboServiceMock)
    }

    @Test
    fun `getAmiibos returns list of amiibos when service responds successfully`() = runTest {
        // Given
        val amiiboList = listOf(
            Amiibo(name = "Mario", image = "url1"),
            Amiibo(name = "Luigi", image = "url2")
        )
        val response = Response.success(Amiibos(amiibo = amiiboList))
        coEvery { amiiboServiceMock.getAmiibos("Mario") } returns response

        // When
        val result = subject.getAmiibos("Mario")

        // Then
        assertEquals(amiiboList, result)
        coVerify { amiiboServiceMock.getAmiibos("Mario") }
    }

//    @Test
//    fun `getAmiibos returns empty list when service responds with null body`() = runTest {
//        // Given
//        val response = Response.success<Amiibos>(null)
//        coEvery { amiiboServiceMock.getAmiibos("Mario") } returns response
//
//        // When
//        val result = subject.getAmiibos("Mario")
//
//        // Then
//        assertEquals(emptyList<Amiibo>(), result)
//        coVerify { amiiboServiceMock.getAmiibos("Mario") }
//    }
}
