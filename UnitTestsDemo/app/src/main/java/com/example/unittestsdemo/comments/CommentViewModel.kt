package com.example.unittestsdemo.comments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CommentViewModel @Inject constructor(
    private val useCase: CommentsUseCase,
) : ViewModel() {

    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments: StateFlow<List<Comment>> = _comments

    init {
        fetchComments()
    }

    fun addComment(text: String) {
        viewModelScope.launch {
            useCase.add(text)
            fetchComments()
        }
    }

    fun fetchComments() {
        viewModelScope.launch {
            _comments.value = useCase.fetch()
        }
    }

    fun updateComment(id: Int, newText: String) {
        viewModelScope.launch {
            useCase.update(id, newText)
            fetchComments()
        }
    }

    fun deleteComment(id: Int) {
        viewModelScope.launch {
            useCase.delete(id)
            fetchComments()
        }
    }
}
