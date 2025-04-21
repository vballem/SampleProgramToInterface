package com.example.unittestsdemo.comments.db.inmemory

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.unittestsdemo.comments.CommentDao
import com.example.unittestsdemo.comments.CommentEntity
import com.example.unittestsdemo.comments.CommentRepository
import com.example.unittestsdemo.comments.CommentRepositoryImpl
import com.example.unittestsdemo.comments.CommentViewModel
import com.example.unittestsdemo.comments.CommentsDatabase
import com.example.unittestsdemo.comments.CommentsUseCase
import com.example.unittestsdemo.comments.CommentsUseCaseImpl
import com.example.unittestsdemo.comments.DatabaseDataSource
import com.example.unittestsdemo.comments.DatabaseDataSourceImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33], application = Application::class)
class CommentViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: CommentsDatabase
    private lateinit var commentDao: CommentDao
    private lateinit var dataSource: DatabaseDataSource
    private lateinit var repository: CommentRepository
    private lateinit var useCase: CommentsUseCase
    private lateinit var viewModel: CommentViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            CommentsDatabase::class.java
        ).allowMainThreadQueries().build()

        commentDao = database.commentDao()
        dataSource = DatabaseDataSourceImpl(commentDao)
        repository = CommentRepositoryImpl(dataSource)
        useCase = CommentsUseCaseImpl(repository)

        viewModel = CommentViewModel(useCase)
    }

    @After
    fun tearDown() {
        database.close()
        Dispatchers.resetMain()
    }

    @Test
    fun `fetch comments retrieves all comments`() = runTest {
        commentDao.addComment(CommentEntity(text = "Comment 1"))

        viewModel.fetchComments()
        advanceUntilIdle()

        val comments = viewModel.comments.first()
        assertEquals(1, comments.size)
        assertEquals("Comment 1", comments[0].text)
    }

    @Test
    fun `add comment updates the list`() = runTest {
        viewModel.addComment("Test Comment")
        advanceUntilIdle()

        val comments = viewModel.comments.first()
        assertEquals(1, comments.size)
        assertEquals("Test Comment", comments[0].text)
    }

    @Test
    fun `delete comment removes it from the list`() = runTest {
        viewModel.addComment("Test Comment")
        advanceUntilIdle()

        val comment = viewModel.comments.first().first()
        viewModel.deleteComment(comment.id)
        advanceUntilIdle()

        val comments = viewModel.comments.first()
        assertEquals(0, comments.size)
    }

    @Test
    fun `update comment modifies the text`() = runTest {
        viewModel.addComment("Old Comment")
        advanceUntilIdle()

        val comment = viewModel.comments.first().first()
        viewModel.updateComment(comment.id, "Updated Comment")
        advanceUntilIdle()

        val updatedComments = viewModel.comments.first()
        assertEquals("Updated Comment", updatedComments[0].text)
    }
}