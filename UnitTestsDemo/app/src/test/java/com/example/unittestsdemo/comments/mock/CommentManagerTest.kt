package com.example.unittestsdemo.comments.mock

import com.example.unittestsdemo.comments.Comment
import com.example.unittestsdemo.comments.CommentManager
import com.example.unittestsdemo.comments.CommentService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

class CommentManagerTest {

    @Test
    fun `should delete only empty comments using Mock`() {
        val mockService = mockk<CommentService>()
        val manager = CommentManager(mockService)//sut

        val comments = listOf(
            Comment(1, ""),
            Comment(2, "Valid"),
            Comment(3, "")
        )

        every { mockService.getComments() } returns comments
        every { mockService.deleteComment(any()) } returns true

        val deletedCount = manager.cleanUpEmptyComments()

        assertEquals(2, deletedCount)
        verify(exactly = 2) { mockService.deleteComment(match { it == 1 || it == 3 }) }
        verify(exactly = 1) { mockService.getComments() }
    }
}
