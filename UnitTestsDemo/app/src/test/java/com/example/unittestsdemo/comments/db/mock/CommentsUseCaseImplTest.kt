package com.example.unittestsdemo.comments.db.mock

import com.example.unittestsdemo.comments.Comment
import com.example.unittestsdemo.comments.CommentRepository
import com.example.unittestsdemo.comments.CommentsUseCaseImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CommentsUseCaseImplTest {

    private lateinit var useCase: CommentsUseCaseImpl
    private val repository: CommentRepository = mockk()

    @Before
    fun setup() {
        useCase = CommentsUseCaseImpl(repository)
    }

    @Test
    fun `fetch comments`() = runTest {
        val comments = listOf(Comment(1, "Test Comment"))
        coEvery { repository.getComments() } returns comments

        val result = useCase.fetch()

        assertEquals(comments, result)
        coVerify { repository.getComments() }
    }

    @Test
    fun `add Comment`() = runTest {
        val comment = Comment(1, "Test Comment")
        coEvery { repository.addComment("Test Comment") } returns comment

        val result = useCase.add("Test Comment")

        assertEquals(comment, result)
        coVerify { repository.addComment("Test Comment") }
    }

    @Test
    fun `update Comment`() = runTest {
        coEvery { repository.updateComment(1, "Updated Comment") } returns true

        val result = useCase.update(1, "Updated Comment")

        assertEquals(true, result)
        coVerify { repository.updateComment(1, "Updated Comment") }
    }

    @Test
    fun `delete Comment`() = runTest {
        coEvery { repository.deleteComment(1) } returns true

        val result = useCase.delete(1)

        assertEquals(true, result)
        coVerify { repository.deleteComment(1) }
    }
}
