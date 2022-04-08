package com.github.adizcode.todo_tada.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.adizcode.todo_tada.model.TodoItem
import com.github.adizcode.todo_tada.model.database.TodoDao
import kotlinx.coroutines.launch

class TodoViewModel(private val todoDao: TodoDao) : ViewModel() {

    val todoList: LiveData<List<TodoItem>>
        get() = todoDao.getAllTodos()

    fun createTodo() {
        val newTodo = TodoItem(0, "Empty Todo", false)
        insertTodo(newTodo)
    }

    private fun insertTodo(todoItem: TodoItem) {
        viewModelScope.launch {
            todoDao.insertTodos(todoItem)
        }
    }

    fun updateTodoTask(todoItem: TodoItem, task: String) {
        todoItem.task = task
        updateTodo(todoItem)
    }

    fun updateTodoDone(todoItem: TodoItem, isDone: Boolean) {
        todoItem.isDone = isDone
        updateTodo(todoItem)
    }

    private fun updateTodo(todoItem: TodoItem) {
        viewModelScope.launch {
            todoDao.updateTodo(todoItem)
        }
    }

    fun deleteTodo(todoItem: TodoItem) {
        viewModelScope.launch {
            todoDao.deleteTodo(todoItem)
        }
    }
}
