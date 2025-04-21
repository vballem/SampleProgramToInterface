package com.example.unittestsdemo.comments.db.mock

import com.example.unittestsdemo.comments.CommentEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class CommentEntityTest {

    @Test
    fun `CommentEntity should correctly assign properties`() {
        val comment = CommentEntity(id = 1, text = "Test Comment")

        assertEquals(1, comment.id)
        assertEquals("Test Comment", comment.text)
    }
}