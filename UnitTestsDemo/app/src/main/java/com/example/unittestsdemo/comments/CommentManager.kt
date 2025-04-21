package com.example.unittestsdemo.comments

class CommentManager(private val commentService: CommentService) {

    fun cleanUpEmptyComments(): Int {
        val comments = commentService.getComments()
        val emptyComments = comments.filter { it.text.isBlank() }
        emptyComments.forEach { commentService.deleteComment(it.id) }
        return emptyComments.size
    }
}