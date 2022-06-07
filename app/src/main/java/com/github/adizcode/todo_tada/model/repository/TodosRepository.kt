package com.github.adizcode.todo_tada.model.repository

import androidx.lifecycle.LiveData
import com.github.adizcode.todo_tada.model.TodoItem
import com.github.adizcode.todo_tada.model.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TodosRepository(private val database: AppDatabase) {
    val incompleteTodos: LiveData<List<TodoItem>> = database.todoDao().getIncompleteTodos()
    val completedTodos: LiveData<List<TodoItem>> = database.todoDao().getCompletedTodos()

    suspend fun insertTodo(todoItem: TodoItem) {
        withContext(Dispatchers.IO) {
            database.todoDao().insertTodos(todoItem)
        }
    }

    suspend fun updateTodo(todoItem: TodoItem) {
        withContext(Dispatchers.IO) {
            database.todoDao().updateTodo(todoItem)
        }
    }

    suspend fun deleteTodo(todoItem: TodoItem) {
        withContext(Dispatchers.IO) {
            database.todoDao().deleteTodo(todoItem)
        }
    }
}
