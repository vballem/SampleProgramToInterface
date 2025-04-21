package com.example.unittestsdemo.comments.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.unittestsdemo.comments.CommentDao
import com.example.unittestsdemo.comments.CommentsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): CommentsDatabase {
        return Room.databaseBuilder(
            context,
            CommentsDatabase::class.java,
            "comments_database"
        ).build()
    }

    @Provides
    fun provideCommentDao(database: CommentsDatabase): CommentDao {
        return database.commentDao()
    }

    @Provides
    @Singleton
    fun provideApplicationContext(application: Application): Context {
        return application.applicationContext
    }
}
