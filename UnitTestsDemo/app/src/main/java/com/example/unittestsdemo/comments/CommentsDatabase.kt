package com.example.unittestsdemo.comments

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CommentEntity::class], version = 1, exportSchema = false)
abstract class CommentsDatabase : RoomDatabase() {
    abstract fun commentDao(): CommentDao
}
