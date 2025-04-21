package com.example.unittestsdemo.comments

import javax.inject.Inject

interface DatabaseDataSource {
    suspend fun addComment(text: String): Comment
    suspend fun getComments(): List<Comment>
    suspend fun updateComment(id: Int, newText: String): Boolean
    suspend fun deleteComment(id: Int): Boolean
}

class DatabaseDataSourceImpl @Inject constructor(private val commentDao: CommentDao) :
    DatabaseDataSource {

    override suspend fun addComment(text: String): Comment {
        val id = commentDao.addComment(CommentEntity(text = text))
        return Comment(id.toInt(), text)
    }

    override suspend fun getComments(): List<Comment> {
        return commentDao.getComments().map { Comment(it.id, it.text) }
    }

    override suspend fun updateComment(id: Int, newText: String): Boolean {
        val rowsUpdated = commentDao.updateComment(CommentEntity(id, newText))
        return rowsUpdated > 0
    }

    override suspend fun deleteComment(id: Int): Boolean {
        val rowsDeleted = commentDao.deleteComment(CommentEntity(id, ""))
        return rowsDeleted > 0
    }
}
