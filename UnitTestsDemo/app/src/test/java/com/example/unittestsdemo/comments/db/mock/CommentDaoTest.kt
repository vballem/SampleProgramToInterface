package com.example.unittestsdemo.comments.db.mock

import com.example.unittestsdemo.comments.CommentDao
import com.example.unittestsdemo.comments.CommentEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CommentDaoTest {

    private lateinit var commentDao: CommentDao

    @Before
    fun setup() {
        commentDao = mockk()
    }

    @Test
    fun `addComment `() = runTest {
        val comment = CommentEntity(text = "Test Comment")
        coEvery { commentDao.addComment(comment) } returns 1L

        val result = commentDao.addComment(comment)

        assertEquals(1L, result)
        coVerify { commentDao.addComment(comment) }
    }

    @Test
    fun `getComments `() = runTest {
        val comments = listOf(CommentEntity(1, "Test Comment"))
        coEvery { commentDao.getComments() } returns comments

        val result = commentDao.getComments()

        assertEquals(comments, result)
        coVerify { commentDao.getComments() }
    }

    @Test
    fun `updateComment `() = runTest {
        val comment = CommentEntity(1, "Updated Comment")
        coEvery { commentDao.updateComment(comment) } returns 1

        val result = commentDao.updateComment(comment)

        assertEquals(1, result)
        coVerify { commentDao.updateComment(comment) }
    }

    @Test
    fun `deleteComment `() = runTest {
        val comment = CommentEntity(1, "Test Comment")
        coEvery { commentDao.deleteComment(comment) } returns 1

        val result = commentDao.deleteComment(comment)

        assertEquals(1, result)
        coVerify { commentDao.deleteComment(comment) }
    }
}