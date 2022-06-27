package com.github.adizcode.todo_tada.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.adizcode.todo_tada.model.TodoItem
import com.github.adizcode.todo_tada.model.repository.TodosRepository
import kotlinx.coroutines.launch

class TodoViewModel(private val todosRepository: TodosRepository) : ViewModel() {

    val incompleteTodos: LiveData<List<TodoItem>> = todosRepository.incompleteTodos

    val completeTodos: LiveData<List<TodoItem>> = todosRepository.completedTodos

    fun createTodo() {
        val newTodo = TodoItem(0, "Empty Todo", false)
        insertTodo(newTodo)
    }

    private fun insertTodo(todoItem: TodoItem) {
        viewModelScope.launch {
            todosRepository.insertTodo(todoItem)
        }
    }

    fun updateTodoTask(todoItem: TodoItem, task: String) {
        updateTodo(todoItem.copy(task = task))
    }

    fun updateTodoDone(todoItem: TodoItem, isDone: Boolean) {
        updateTodo(todoItem.copy(isDone = isDone))
    }

    private fun updateTodo(todoItem: TodoItem) {
        viewModelScope.launch {
            todosRepository.updateTodo(todoItem)
        }
    }

    fun deleteTodo(todoItem: TodoItem) {
        viewModelScope.launch {
            todosRepository.deleteTodo(todoItem)
        }
    }
}
