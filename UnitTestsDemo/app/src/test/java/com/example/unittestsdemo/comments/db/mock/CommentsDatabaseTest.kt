package com.example.unittestsdemo.comments.db.mock

import com.example.unittestsdemo.comments.CommentDao
import com.example.unittestsdemo.comments.CommentsDatabase
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class CommentsDatabaseTest {

    private lateinit var database: CommentsDatabase
    private lateinit var commentDao: CommentDao

    @Before
    fun setup() {
        commentDao = mockk()
        database = mockk {
            every { commentDao() } returns commentDao
        }
    }

    @Test
    fun `return valid DAO`() {
        val dao = database.commentDao()
        assertNotNull(dao)
    }
}