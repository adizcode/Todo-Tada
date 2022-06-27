package com.github.adizcode.todo_tada.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.github.adizcode.todo_tada.MyApplication
import com.github.adizcode.todo_tada.model.repository.TodosRepository
import com.github.adizcode.todo_tada.viewmodel.TodoViewModel
import com.github.adizcode.todo_tada.viewmodel.TodoViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val database = (applicationContext as MyApplication).database
            val viewModel by viewModels<TodoViewModel> { TodoViewModelFactory(TodosRepository(database)) }

            TodoTada(viewModel)
        }
    }
}
