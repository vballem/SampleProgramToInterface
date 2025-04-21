package com.example.unittestsdemo.comments.db.mock

import com.example.unittestsdemo.comments.Comment
import com.example.unittestsdemo.comments.CommentDao
import com.example.unittestsdemo.comments.CommentEntity
import com.example.unittestsdemo.comments.DatabaseDataSourceImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DatabaseDataSourceImplTest {

    private lateinit var dataSource: DatabaseDataSourceImpl
    private val commentDao: CommentDao = mockk()

    @Before
    fun setup() {
        dataSource = DatabaseDataSourceImpl(commentDao)
    }

    @Test
    fun `addComment should call dao and return Comment`() = runTest {
        val commentEntity = CommentEntity(text = "Test Comment")
        coEvery { commentDao.addComment(commentEntity) } returns 1L

        val result = dataSource.addComment("Test Comment")

        assertEquals(Comment(1, "Test Comment"), result)
        coVerify { commentDao.addComment(commentEntity) }
    }

    @Test
    fun `getComments should call dao and return list of Comments`() = runTest {
        val commentEntities = listOf(
            CommentEntity(id = 1, text = "Comment 1"),
            CommentEntity(id = 2, text = "Comment 2")
        )
        coEvery { commentDao.getComments() } returns commentEntities

        val result = dataSource.getComments()

        assertEquals(
            listOf(
                Comment(1, "Comment 1"),
                Comment(2, "Comment 2")
            ),
            result
        )
        coVerify { commentDao.getComments() }
    }

    @Test
    fun `updateComment should call dao and return true if successful`() = runTest {
        val commentEntity = CommentEntity(id = 1, text = "Updated Comment")
        coEvery { commentDao.updateComment(commentEntity) } returns 1

        val result = dataSource.updateComment(1, "Updated Comment")

        assertEquals(true, result)
        coVerify { commentDao.updateComment(commentEntity) }
    }

    @Test
    fun `deleteComment should call dao and return true if successful`() = runTest {
        val commentEntity = CommentEntity(id = 1, text = "")
        coEvery { commentDao.deleteComment(commentEntity) } returns 1

        val result = dataSource.deleteComment(1)

        assertEquals(true, result)
        coVerify { commentDao.deleteComment(commentEntity) }
    }
}
