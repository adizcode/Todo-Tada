package com.github.adizcode.todo_tada.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.github.adizcode.todo_tada.MyApplication
import com.github.adizcode.todo_tada.viewmodel.TodoViewModel
import com.github.adizcode.todo_tada.viewmodel.TodoViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val todoDao = (applicationContext as MyApplication).database.todoDao()
            val viewModel by viewModels<TodoViewModel> { TodoViewModelFactory(todoDao) }

            TodoTada(viewModel)
        }
    }
}
