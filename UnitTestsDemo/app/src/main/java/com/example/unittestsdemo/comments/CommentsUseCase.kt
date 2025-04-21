package com.example.unittestsdemo.comments

import javax.inject.Inject


interface CommentsUseCase {
    suspend fun fetch(): List<Comment>
    suspend fun add(text: String): Comment
    suspend fun update(id: Int, newText: String): Boolean
    suspend fun delete(id: Int): Boolean
}

class CommentsUseCaseImpl @Inject constructor(private val repository: CommentRepository) :
    CommentsUseCase {

    override suspend fun fetch(): List<Comment> = repository.getComments()
    override suspend fun add(text: String): Comment = repository.addComment(text)
    override suspend fun update(id: Int, newText: String): Boolean =
        repository.updateComment(id, newText)

    override suspend fun delete(id: Int): Boolean = repository.deleteComment(id)
}