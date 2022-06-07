package com.github.adizcode.todo_tada.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.adizcode.todo_tada.model.repository.TodosRepository

class TodoViewModelFactory(private val repository: TodosRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(TodosRepository::class.java)
            .newInstance(repository)
    }
}
