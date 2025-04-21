package com.example.unittestsdemo.amiibo.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

@InstallIn(SingletonComponent::class)
@Module
class DispatchersModule {
    @Provides
    @IODispatcher
    fun providesIODispatcher(): CoroutineDispatcher = Dispatchers.IO
}

@Qualifier
annotation class IODispatcher
