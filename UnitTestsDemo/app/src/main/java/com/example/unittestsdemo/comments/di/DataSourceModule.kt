package com.example.unittestsdemo.comments.di

import com.example.unittestsdemo.comments.DatabaseDataSource
import com.example.unittestsdemo.comments.DatabaseDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindDatabaseDataSource(
        impl: DatabaseDataSourceImpl
    ): DatabaseDataSource
}
