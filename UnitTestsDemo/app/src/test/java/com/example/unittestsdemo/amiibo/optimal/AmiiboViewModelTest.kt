package com.example.unittestsdemo.amiibo.optimal

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.unittestsdemo.amiibo.AmiiboIntent
import com.example.unittestsdemo.amiibo.AmiiboService
import com.example.unittestsdemo.amiibo.AmiiboState
import com.example.unittestsdemo.amiibo.AmiiboViewModel
import com.example.unittestsdemo.amiibo.AmiibosNetworkDataSource
import com.example.unittestsdemo.amiibo.AmiibosNetworkDataSourceImpl
import com.example.unittestsdemo.amiibo.AmiibosRepository
import com.example.unittestsdemo.amiibo.AmiibosRepositoryImpl
import com.example.unittestsdemo.amiibo.GetAmiibosUseCase
import com.example.unittestsdemo.amiibo.GetAmiibosUseCaseImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

@OptIn(ExperimentalCoroutinesApi::class)
class AmiiboViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private lateinit var mockWebServer: MockWebServer
    private lateinit var viewModel: AmiiboViewModel

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
        val dataSource: AmiibosNetworkDataSource = AmiibosNetworkDataSourceImpl(service)
        val repo: AmiibosRepository = AmiibosRepositoryImpl(dataSource, testDispatcher)
        val useCase: GetAmiibosUseCase = GetAmiibosUseCaseImpl(repo)
        viewModel = AmiiboViewModel(useCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mockWebServer.shutdown()
    }

    @Test
    fun `getAmiibos emits loading state initially`() = testScope.runTest {
        // When
        viewModel.sendIntent(AmiiboIntent.FetchAmiibos("Mario"))
        val result = viewModel.state.first { it is AmiiboState.Loading }

        // Then
        assert(result is AmiiboState.Loading)
    }

    @Test
    fun `getAmiibos returns list of amiibos when API returns data`() = testScope.runTest {
        // Given
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(getJson("amiibos_success_response.json"))
        mockWebServer.enqueue(mockResponse)

        // When
        viewModel.sendIntent(AmiiboIntent.FetchAmiibos("Mario"))
        val result =
            (viewModel.state.first { it is AmiiboState.Success } as AmiiboState.Success).data


        // Then
        assertEquals(2, result.size)
        assertEquals("Mario - Tennis", result[0].name)
        assertEquals("url_1", result[0].image)
        assert(viewModel.state.first() is AmiiboState.Success)
    }

    private fun getJson(path: String): String {
        val uri = this::class.java.classLoader!!.getResource(path)
        return uri.readText()
    }


    @Test
    fun `getAmiibos returns empty list when API returns empty response`() = testScope.runTest {
        // Given
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(getJson("amiibos_empty_response.json"))
        mockWebServer.enqueue(mockResponse)

        // When
        viewModel.sendIntent(AmiiboIntent.FetchAmiibos("Mario"))
        val result =
            (viewModel.state.first { it is AmiiboState.Success } as AmiiboState.Success).data

        // Then
        assertEquals(0, result.size)
    }

    @Test
    fun `getAmiibos emits error state when API returns malformed JSON`() = testScope.runTest {
        // Given
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(getJson("amiibos_error_response.json"))
        mockWebServer.enqueue(mockResponse)

        // When
        viewModel.sendIntent(AmiiboIntent.FetchAmiibos("Mario"))
        val result = viewModel.state.first { it is AmiiboState.Error }

        // Then
        assert(result is AmiiboState.Error)
        assertEquals(AmiiboState.Error, result)
    }

    @Test
    fun `getAmiibos emits error state when API returns 404`() = testScope.runTest {
        // Given
        val mockResponse = MockResponse()
            .setResponseCode(404)
        mockWebServer.enqueue(mockResponse)

        // When
        viewModel.sendIntent(AmiiboIntent.FetchAmiibos("Mario"))
        val result = viewModel.state.first { it is AmiiboState.Error }

        // Then
        assert(result is AmiiboState.Error)
    }

    @Test
    fun `getAmiibos emits error state when API returns 500`() = testScope.runTest {
        // Given
        val mockResponse = MockResponse()
            .setResponseCode(500)
        mockWebServer.enqueue(mockResponse)

        // When
        viewModel.sendIntent(AmiiboIntent.FetchAmiibos("Mario"))
        val result = viewModel.state.first { it is AmiiboState.Error }

        // Then
        assert(result is AmiiboState.Error)
    }

    @Test
    fun `getAmiibos returns empty list when API returns null amiibo`() = testScope.runTest {
        // Given
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("{ \"amiibo\": null }")
        mockWebServer.enqueue(mockResponse)

        // When
        viewModel.sendIntent(AmiiboIntent.FetchAmiibos("Mario"))
        val result =
            (viewModel.state.first { it is AmiiboState.Success } as AmiiboState.Success).data

        // Then
        assertEquals(0, result.size)
    }

    @Test
    fun `getAmiibos throws exception when API returns error response`() = testScope.runTest {
        // Given
        val mockResponse = MockResponse()
            .setResponseCode(400)
            .setBody("Bad Request")
        mockWebServer.enqueue(mockResponse)

        // When
        viewModel.sendIntent(AmiiboIntent.FetchAmiibos("Mario"))
        val result = viewModel.state.first { it is AmiiboState.Error }

        // Then
        assert(result is AmiiboState.Error)
    }

}
