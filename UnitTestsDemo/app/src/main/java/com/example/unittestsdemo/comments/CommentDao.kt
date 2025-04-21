package com.example.unittestsdemo.comments

import androidx.room.*

@Dao
interface CommentDao {
    @Insert
    suspend fun addComment(comment: CommentEntity): Long

    @Query("SELECT * FROM comments")
    suspend fun getComments(): List<CommentEntity>

    @Update
    suspend fun updateComment(comment: CommentEntity): Int

    @Delete
    suspend fun deleteComment(comment: CommentEntity): Int
}