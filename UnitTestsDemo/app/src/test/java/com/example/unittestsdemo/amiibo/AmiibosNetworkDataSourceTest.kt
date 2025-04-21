package com.example.unittestsdemo.amiibo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.last
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

@OptIn(ExperimentalCoroutinesApi::class)
class AmiibosNetworkDataSourceTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private lateinit var mockWebServer: MockWebServer
    private lateinit var useCase: GetAmiibosUseCase

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
        val dataSource = AmiibosNetworkDataSourceImpl(service)
        val repo = AmiibosRepositoryImpl(dataSource, testDispatcher)
        useCase = GetAmiibosUseCaseImpl(repo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mockWebServer.shutdown()
    }

    @Test
    fun `getAmiibos returns list of amiibos when API returns data`() = testScope.runTest {
        // Given
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(getJson("amiibos_success_response.json"))
        mockWebServer.enqueue(mockResponse)

        // When
        val result = useCase("Mario").last()

        // Then
        assertEquals(2, result.size)
        assertEquals("Metal Mario - Tennis", result[0].name)
    }

    private fun getJson(path: String): String {
        val uri = this::class.java.classLoader!!.getResource(path)
        return uri.readText()
    }
}