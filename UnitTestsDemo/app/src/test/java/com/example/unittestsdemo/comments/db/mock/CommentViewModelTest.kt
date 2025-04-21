package com.example.unittestsdemo.comments.db.mock

import com.example.unittestsdemo.comments.Comment
import com.example.unittestsdemo.comments.CommentViewModel
import com.example.unittestsdemo.comments.CommentsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CommentViewModelTest {

    private lateinit var viewModel: CommentViewModel
    private val useCase: CommentsUseCase = mockk()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        coEvery { useCase.fetch() } returns emptyList() // Default behavior for fetch
        viewModel = CommentViewModel(useCase)
    }

    @Test
    fun `fetch comments`() = runTest(testDispatcher) {
        val comments = listOf(Comment(1, "Test Comment"))
        coEvery { useCase.fetch() } returns comments

        viewModel.fetchComments()
        val result = viewModel.comments.first { it.isNotEmpty() }

        assertEquals(comments, result)
        coVerify { useCase.fetch() }
    }

    @Test
    fun `add comment`() = runTest(testDispatcher) {
        coEvery { useCase.add("Test Comment") } returns Comment(1, "Test Comment")
        coEvery { useCase.fetch() } returns listOf(Comment(1, "Test Comment"))

        viewModel.addComment("Test Comment")
        val result = viewModel.comments.first()

        assertEquals(1, result.size)
        assertEquals("Test Comment", result[0].text)
        coVerify { useCase.add("Test Comment") }
    }

    @Test
    fun `update comment`() = runTest(testDispatcher) {
        coEvery { useCase.update(1, "Updated Comment") } returns true
        coEvery { useCase.fetch() } returns listOf(Comment(1, "Updated Comment"))

        viewModel.updateComment(1, "Updated Comment")
        val result = viewModel.comments.first { it.isNotEmpty() } // Wait for non-empty list

        assertEquals(1, result.size)
        assertEquals("Updated Comment", result[0].text)
        coVerify { useCase.update(1, "Updated Comment") }
    }

    @Test
    fun `delete comment `() = runTest(testDispatcher) {
        coEvery { useCase.delete(1) } returns true
        coEvery { useCase.fetch() } returns emptyList()

        viewModel.deleteComment(1)
        val result = viewModel.comments.first()

        assertEquals(0, result.size)
        coVerify { useCase.delete(1) }
    }
}
