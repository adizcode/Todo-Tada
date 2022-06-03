package com.github.adizcode.todo_tada.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Checkbox
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

                val incompleteTodos by viewModel.incompleteTodos.observeAsState(listOf())
                val completeTodos by viewModel.completeTodos.observeAsState(listOf())
                val horizontalPadding = 16.dp

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = horizontalPadding)
                ) {
                    TodoHeader(
                        incompleteTodosCount = incompleteTodos.size,
                        completeTodosCount = completeTodos.size,
                        modifier = Modifier.padding(vertical = horizontalPadding * 3 / 4)
                    )
                    Divider()
                    TodoItemLists(
                        incompleteTodos = incompleteTodos,
                        completeTodos = completeTodos,
                        onTaskChange = viewModel::updateTodoTask,
                        onDoneChange = viewModel::updateTodoDone,
                        onDeleteTodo = viewModel::deleteTodo,
                        listModifier = Modifier.padding(vertical = horizontalPadding / 2)
                    )
                }
            }
        }
    }
}

@Composable
fun TodoHeader(incompleteTodosCount: Int, completeTodosCount: Int, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            "March 9, 2020",
            style = MaterialTheme.typography.h4
        )
        Text(
            "$incompleteTodosCount incomplete, $completeTodosCount complete",
            style = MaterialTheme.typography.subtitle2,
        )
    }
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
fun TodoItemLists(
    incompleteTodos: List<TodoItem>,
    completeTodos: List<TodoItem>,
    onTaskChange: (TodoItem, String) -> Unit,
    onDoneChange: (TodoItem, Boolean) -> Unit,
    onDeleteTodo: (TodoItem) -> Unit,
    listModifier: Modifier = Modifier
) {
    Column {
        TodoItemList(
            heading = "Incomplete",
            todoList = incompleteTodos,
            onTaskChange = onTaskChange,
            onDoneChange = onDoneChange,
            onDeleteTodo = onDeleteTodo,
            modifier = listModifier
        )
        TodoItemList(
            heading = "Complete",
            todoList = completeTodos,
            onTaskChange = onTaskChange,
            onDoneChange = onDoneChange,
            onDeleteTodo = onDeleteTodo,
            modifier = listModifier
        )
    }
}

@Composable
fun TodoItemList(
    heading: String,
    todoList: List<TodoItem>,
    onTaskChange: (TodoItem, String) -> Unit,
    onDoneChange: (TodoItem, Boolean) -> Unit,
    onDeleteTodo: (TodoItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(heading, style = MaterialTheme.typography.h6)
        LazyColumn(reverseLayout = true) {
            items(
                items = todoList,
                key = { it.id }
            ) { todoItem ->
                DismissibleTodoItemRow(
                    task = todoItem.task,
                    onTaskChange = { task -> onTaskChange(todoItem, task) },
                    isDone = todoItem.isDone,
                    onDoneChange = { isDone -> onDoneChange(todoItem, isDone) },
                    onDismiss = { onDeleteTodo(todoItem) }
                )
            }
        }
    }
}

@Composable
fun DismissibleTodoItemRow(
    task: String,
    onTaskChange: (String) -> Unit,
    isDone: Boolean,
    onDoneChange: (Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    DismissibleStateful(onDismiss = onDismiss) {
        TodoItemRowStateful(
            task = task,
            onTaskChange = onTaskChange,
            isDone = isDone,
            onDoneChange = onDoneChange
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DismissibleStateful(onDismiss: () -> Unit, dismissContent: @Composable RowScope.() -> Unit) {
    val dismissState = rememberDismissState(
        confirmStateChange = { dismissValue ->
            if (dismissValue == DismissValue.DismissedToStart) {
                onDismiss()
                return@rememberDismissState true
            }
            false
        }
    )

    SwipeToDismiss(
        state = dismissState,
        background = {
            val color = when (dismissState.dismissDirection) {
                DismissDirection.EndToStart -> Color.Transparent
                DismissDirection.StartToEnd -> Color.Transparent
                null -> Color.Transparent
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .background(color)
            )
        },
        dismissContent = dismissContent,
        directions = setOf(DismissDirection.EndToStart)
    )
}

@Composable
fun TodoItemRowStateful(
    task: String,
    onTaskChange: (String) -> Unit,
    isDone: Boolean,
    onDoneChange: (Boolean) -> Unit
) {

    // Duplicated state for the UI to remember
    val (taskNameInUi, setTaskNameInUi) = remember { mutableStateOf(task) }
    val (isTaskDoneInUi, setTaskDoneInUi) = remember { mutableStateOf(isDone) }

    TodoItemRow(
        task = taskNameInUi,
        onTaskChange = { taskNameInUiUpdated ->
            setTaskNameInUi(taskNameInUiUpdated)
            onTaskChange(taskNameInUiUpdated)
        },
        isDone = isTaskDoneInUi,
        onDoneChange = { isTaskDoneInUiUpdated ->
            setTaskDoneInUi(isTaskDoneInUiUpdated)
            onDoneChange(isTaskDoneInUiUpdated)
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
            .background(MaterialTheme.colors.surface)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isDone,
            onCheckedChange = onDoneChange,
        )
        Spacer(Modifier.width(16.dp))
        BasicTextField(
            value = task,
            onValueChange = onTaskChange,
            textStyle = MaterialTheme.typography.body1
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TodoHeaderPreview() {
    TodoTadaTheme {
        TodoHeader(incompleteTodosCount = 5, completeTodosCount = 5)
    }
}

@Preview(showBackground = true)
@Composable
private fun TodoItemListPreview() {
    TodoTadaTheme {
        TodoItemList(
            heading = "Todos",
            todoList = listOf(
                TodoItem(0, "Write Blog", false),
                TodoItem(0, "Play Guitar", true),
                TodoItem(0, "Learn Jetpack Compose", true)
            ),
            onTaskChange = { _, _ -> },
            onDoneChange = { _, _ -> },
            onDeleteTodo = { }
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
