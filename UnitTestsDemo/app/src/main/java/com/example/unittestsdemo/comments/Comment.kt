package com.example.unittestsdemo.comments

data class Comment(val id: Int, val text: String)

interface CommentService {
    fun addComment(text: String): Comment
    fun getComments(): List<Comment>
    fun updateComment(id: Int, newText: String): Boolean
    fun deleteComment(id: Int): Boolean
}