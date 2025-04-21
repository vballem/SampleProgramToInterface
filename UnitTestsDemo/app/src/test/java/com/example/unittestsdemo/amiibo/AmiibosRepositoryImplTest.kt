package com.example.unittestsdemo.amiibo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class AmiibosRepositoryImplTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private lateinit var repository: AmiibosRepository
    private lateinit var dataSource: AmiibosNetworkDataSource
    private val dispatcher: CoroutineDispatcher = StandardTestDispatcher()

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        mockWebServer = MockWebServer()
        mockWebServer.start()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(AmiiboService::class.java)

        dataSource = AmiibosNetworkDataSourceImpl(service)
        repository = AmiibosRepositoryImpl(dataSource, dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mockWebServer.shutdown()
    }

    @Test
    fun `getAmiibos returns list of amiibos when API provides data`() = testScope.runTest {
        // Given
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(getJson("amiibos_success_response.json"))
        mockWebServer.enqueue(mockResponse)

        // When
        val result = repository.getAmiibos("Mario").first()

        // Then
        val expected = listOf(
            Amiibo(name = "Metal Mario - Tennis", image = "dummy_url"),
            Amiibo(name = "Metal Mario - Tennis", image = "dummy_url")
        )
        assertEquals(expected, result)
    }

    @Test
    fun `getAmiibos throws exception when API returns error`() = testScope.runTest {
        // Given
        mockWebServer.enqueue(MockResponse().setResponseCode(500))

        // When & Assert
        try {
            repository.getAmiibos("Mario").first()
        } catch (e: Exception) {
            assertEquals("HTTP 500 Server Error", e.message)
        }
    }

    private fun getJson(path: String): String {
        val uri = this::class.java.classLoader!!.getResource(path)
        return uri.readText()
    }
}