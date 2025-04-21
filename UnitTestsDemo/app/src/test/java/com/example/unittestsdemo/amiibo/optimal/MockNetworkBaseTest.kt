package com.example.unittestsdemo.amiibo.optimal

import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.nio.charset.StandardCharsets

abstract class MockNetworkBaseTest {

    protected val mockWebServer = MockWebServer()
    lateinit var url: String
    fun startServer() {
        mockWebServer.start()
        url = mockWebServer.url("/").toUri().toString()
    }

    inline fun <reified T> createService(): T {
        val client: OkHttpClient =
            OkHttpClient
                .Builder()
                .build()

        return Retrofit
            .Builder()
            .client(client)
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(T::class.java)
    }

    protected fun givenResponse(responseJson: String) {
        val inputStream = javaClass.classLoader!!.getResourceAsStream(responseJson)

        val body = inputStream.source().buffer()
        val response = MockResponse().setBody(body.readString(StandardCharsets.UTF_8))
            .setResponseCode(200)

        mockWebServer.enqueue(response)
    }
}