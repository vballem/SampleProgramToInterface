package com.example.unittestsdemo.comments.db.mock

import com.example.unittestsdemo.comments.Comment
import com.example.unittestsdemo.comments.CommentRepositoryImpl
import com.example.unittestsdemo.comments.DatabaseDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CommentRepositoryImplTest {

    private lateinit var repository: CommentRepositoryImpl
    private val dataSource: DatabaseDataSource = mockk()

    @Before
    fun setup() {
        repository = CommentRepositoryImpl(dataSource)
    }

    @Test
    fun `addComment `() = runTest {
        val comment = Comment(1, "Test Comment")
        coEvery { dataSource.addComment("Test Comment") } returns comment

        val result = repository.addComment("Test Comment")

        assertEquals(comment, result)
        coVerify { dataSource.addComment("Test Comment") }
    }

    @Test
    fun `getComments`() = runTest {
        val comments = listOf(Comment(1, "Test Comment"))
        coEvery { dataSource.getComments() } returns comments

        val result = repository.getComments()

        assertEquals(comments, result)
        coVerify { dataSource.getComments() }
    }

    @Test
    fun `updateComment `() = runTest {
        coEvery { dataSource.updateComment(1, "Updated Comment") } returns true

        val result = repository.updateComment(1, "Updated Comment")

        assertEquals(true, result)
        coVerify { dataSource.updateComment(1, "Updated Comment") }
    }

    @Test
    fun `deleteComment `() = runTest {
        coEvery { dataSource.deleteComment(1) } returns true

        val result = repository.deleteComment(1)

        assertEquals(true, result)
        coVerify { dataSource.deleteComment(1) }
    }
}