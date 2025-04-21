package com.example.unittestsdemo.comments.fake

import com.example.unittestsdemo.comments.Comment
import com.example.unittestsdemo.comments.CommentService

class FakeCommentService : CommentService {
    private val comments = mutableListOf<Comment>()
    private var idCounter = 1

    override fun addComment(text: String): Comment {
        val comment = Comment(idCounter++, text)
        comments.add(comment)
        return comment
    }

    override fun getComments(): List<Comment> = comments.toList()

    override fun updateComment(id: Int, newText: String): Boolean {
        val index = comments.indexOfFirst { it.id == id }
        if (index != -1) {
            comments[index] = comments[index].copy(text = newText)
            return true
        }
        return false
    }

    override fun deleteComment(id: Int): Boolean {
        return comments.removeIf { it.id == id }
    }
}
