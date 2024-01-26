package com.example.pitchcatalystassignment.domain.repository

import com.example.pitchcatalystassignment.domain.model.TodoItem

interface TodoRepository {

    suspend fun getTodoItems(): List<TodoItem>

    suspend fun addTodoItem(title: String, body: String)

    suspend fun deleteCheckedTodoItems(ids: List<String>)
}
