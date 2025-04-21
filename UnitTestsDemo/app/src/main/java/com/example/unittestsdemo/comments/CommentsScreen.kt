package com.example.unittestsdemo.comments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun CommentsScreen(
    viewModel: CommentViewModel = hiltViewModel()
) {
    var newComment by remember { mutableStateOf("") }
    val comments by viewModel.comments.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Input Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(value = newComment,
                onValueChange = { newComment = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Enter comment") })
            Button(onClick = {
                if (newComment.isNotBlank()) {
                    viewModel.addComment(newComment)
                    newComment = ""
                }
            }) {
                Text("Save")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Comments List
        if (comments.isEmpty()) {
            Text(
                text = "No comments available",
                style = androidx.compose.ui.text.TextStyle(fontSize = 18.sp),
                modifier = Modifier.padding(16.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(comments) { comment ->
                    CommentItem(comment = comment,
                        onDelete = { viewModel.deleteComment(comment.id) })
                }
            }
        }
    }
}

@Composable
fun CommentItem(
    comment: Comment, onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(color = androidx.compose.ui.graphics.Color.Magenta), // Add background color
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = comment.text,
            style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp),
            modifier = Modifier.weight(1f)
        )
        Button(onClick = onDelete) {
            Text("Delete")
        }
    }
}