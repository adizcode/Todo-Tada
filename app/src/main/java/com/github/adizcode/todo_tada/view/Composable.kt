package com.github.adizcode.todo_tada.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Checkbox
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.adizcode.todo_tada.model.TodoItem
import com.github.adizcode.todo_tada.view.theme.TodoTadaTheme
import com.github.adizcode.todo_tada.viewmodel.TodoViewModel

@Composable
fun TodoTada(viewModel: TodoViewModel) {
    TodoTadaTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Scaffold(floatingActionButton = {
                NewTodoButton(onCreateTodo = viewModel::createTodo)
            }) {

                val todoList by viewModel.todoList.observeAsState(listOf())

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    TodoHeader(todoList.size)
                    TodoItemList(
                        todoList = todoList,
                        onTaskChange = viewModel::updateTodoTask,
                        onDoneChange = viewModel::updateTodoDone
                    )
                }
            }
        }
    }
}

@Composable
fun TodoHeader(count: Int) {
    Text(
        "You have $count todos",
        style = MaterialTheme.typography.h4,
        modifier = Modifier.padding(8.dp)
    )
}

@Composable
fun NewTodoButton(onCreateTodo: () -> Unit) {
    FloatingActionButton(
        contentColor = Color.White,
        onClick = onCreateTodo
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "Create a new Todo"
        )
    }
}

@Composable
fun TodoItemList(
    todoList: List<TodoItem>,
    onTaskChange: (TodoItem, String) -> Unit,
    onDoneChange: (TodoItem, Boolean) -> Unit
) {
    LazyColumn(reverseLayout = true) {
        items(items = todoList) { todoItem ->
            TodoItemRowStateful(
                task = todoItem.task,
                onTaskChange = { updatedTask ->
                    onTaskChange(todoItem, updatedTask)
                },
                isDone = todoItem.isDone,
                onDoneChange = { isDoneUpdated ->
                    onDoneChange(todoItem, isDoneUpdated)
                }
            )
        }
    }
}

@Composable
fun TodoItemRowStateful(
    task: String,
    onTaskChange: (String) -> Unit,
    isDone: Boolean,
    onDoneChange: (Boolean) -> Unit
) {

    // Duplicated state for the UI to remember
    val (visibleTask, setVisibleTask) = rememberSaveable { mutableStateOf(task) }
    val (isVisibleDone, setVisibleDone) = rememberSaveable { mutableStateOf(isDone) }

    TodoItemRow(
        task = visibleTask,
        onTaskChange = { visibleTaskUpdated ->
            setVisibleTask(visibleTaskUpdated)
            onTaskChange(visibleTaskUpdated)
        },
        isDone = isVisibleDone,
        onDoneChange = { isVisibleDoneUpdated ->
            setVisibleDone(isVisibleDoneUpdated)
            onDoneChange(isVisibleDoneUpdated)
        }
    )
}

@Composable
fun TodoItemRow(
    task: String,
    onTaskChange: (String) -> Unit,
    isDone: Boolean,
    onDoneChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = task,
            onValueChange = onTaskChange,
            Modifier.width(275.dp)
        )
        Checkbox(
            checked = isDone,
            onCheckedChange = onDoneChange,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TodoHeaderPreview() {
    TodoTadaTheme {
        TodoHeader(5)
    }
}

@Preview(showBackground = true)
@Composable
private fun TodoItemListPreview() {
    TodoTadaTheme {
        TodoItemList(
            todoList = listOf(
                TodoItem(0, "Write Blog", false),
                TodoItem(0, "Play Guitar", true),
                TodoItem(0, "Learn Jetpack Compose", true)
            ),
            onTaskChange = { _, _ -> },
            onDoneChange = { _, _ -> }
        )
    }
}

@Preview
@Composable
private fun NewTodoButtonPreview() {
    TodoTadaTheme {
        NewTodoButton {
        }
    }
}