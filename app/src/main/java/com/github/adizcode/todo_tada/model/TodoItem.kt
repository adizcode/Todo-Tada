package com.github.adizcode.todo_tada.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_items")
data class TodoItem(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo val task: String,
    @ColumnInfo(name = "is_done") val isDone: Boolean
)
