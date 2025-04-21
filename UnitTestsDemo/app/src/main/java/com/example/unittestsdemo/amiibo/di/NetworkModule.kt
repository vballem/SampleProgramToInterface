package com.example.unittestsdemo.amiibo.di

import com.example.unittestsdemo.amiibo.AmiiboService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideApiService(): AmiiboService {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)

        val client: OkHttpClient =
            OkHttpClient
                .Builder()
                .retryOnConnectionFailure(true)
                .addInterceptor(logging)
                .build()

        return Retrofit
            .Builder()
            .client(client)
            .baseUrl("https://www.amiiboapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AmiiboService::class.java)
    }

}
