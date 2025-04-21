package com.example.unittestsdemo.amiibo.di

import com.example.unittestsdemo.comments.CommentRepository
import com.example.unittestsdemo.comments.CommentRepositoryImpl
import com.example.unittestsdemo.comments.CommentsUseCase
import com.example.unittestsdemo.comments.CommentsUseCaseImpl
import com.example.unittestsdemo.amiibo.AmiibosNetworkDataSource
import com.example.unittestsdemo.amiibo.AmiibosNetworkDataSourceImpl
import com.example.unittestsdemo.amiibo.AmiibosRepository
import com.example.unittestsdemo.amiibo.AmiibosRepositoryImpl
import com.example.unittestsdemo.amiibo.GetAmiibosUseCase
import com.example.unittestsdemo.amiibo.GetAmiibosUseCaseImpl
import com.example.unittestsdemo.util.ConnectivityManagerNetworkMonitor
import com.example.unittestsdemo.util.NetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface AppModule {
    @Binds
    fun bindDataSource(dataSourceImpl: AmiibosNetworkDataSourceImpl): AmiibosNetworkDataSource

    @Binds
    fun bindAmiibosRepository(repository: AmiibosRepositoryImpl): AmiibosRepository

    @Binds
    fun bindsCommentRepository(repository: CommentRepositoryImpl): CommentRepository

    @Binds
    fun bindGetAmiibosUseCase(useCase: GetAmiibosUseCaseImpl): GetAmiibosUseCase

    @Binds
    fun bindCommentsUseCase(useCase: CommentsUseCaseImpl): CommentsUseCase

    @Binds
    fun bindsNetworkMonitor(
        networkMonitor: ConnectivityManagerNetworkMonitor,
    ): NetworkMonitor
}
