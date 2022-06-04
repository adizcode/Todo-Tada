package com.github.adizcode.todo_tada.model.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.github.adizcode.todo_tada.model.TodoItem

@Dao
interface TodoDao {
    @Insert
    suspend fun insertTodos(vararg todoItems: TodoItem)

    @Update
    suspend fun updateTodo(todoItem: TodoItem)

    @Delete
    suspend fun deleteTodo(todoItem: TodoItem)

    @Query("SELECT * FROM todo_items WHERE is_done = 0")
    fun getIncompleteTodos(): LiveData<List<TodoItem>>

    @Query("SELECT * FROM todo_items WHERE is_done = 1")
    fun getCompletedTodos(): LiveData<List<TodoItem>>

    @Query("SELECT * FROM todo_items")
    fun getAllTodos(): LiveData<List<TodoItem>>
}
