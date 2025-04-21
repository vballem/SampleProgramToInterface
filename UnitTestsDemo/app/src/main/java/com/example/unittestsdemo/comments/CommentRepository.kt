package com.example.unittestsdemo.comments

import javax.inject.Inject

interface CommentRepository {
    suspend fun addComment(text: String): Comment
    suspend fun getComments(): List<Comment>
    suspend fun updateComment(id: Int, newText: String): Boolean
    suspend fun deleteComment(id: Int): Boolean
}

class CommentRepositoryImpl @Inject constructor(private val dataSource: DatabaseDataSource) :
    CommentRepository {

    override suspend fun addComment(text: String): Comment = dataSource.addComment(text)

    override suspend fun getComments(): List<Comment> = dataSource.getComments()

    override suspend fun updateComment(id: Int, newText: String): Boolean = dataSource.updateComment(id, newText)

    override suspend fun deleteComment(id: Int): Boolean = dataSource.deleteComment(id)
}