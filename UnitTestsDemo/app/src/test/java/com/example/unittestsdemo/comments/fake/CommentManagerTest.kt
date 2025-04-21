package com.example.unittestsdemo.comments.fake

import com.example.unittestsdemo.comments.CommentManager
import org.junit.Assert.assertEquals
import org.junit.Test

class CommentManagerTest {

    @Test
    fun `should delete empty comments using Fake`() {
        val fakeService = FakeCommentService()
        val manager = CommentManager(fakeService)

        fakeService.addComment("Hello")
        fakeService.addComment("")
        fakeService.addComment("World")
        fakeService.addComment("")

        val deletedCount = manager.cleanUpEmptyComments()

        assertEquals(2, deletedCount)
        assertEquals(2, fakeService.getComments().size)
    }

}