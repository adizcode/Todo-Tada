package com.github.adizcode.todo_tada

import android.app.Application
import com.github.adizcode.todo_tada.model.database.AppDatabase

class MyApplication : Application() {
    val database by lazy { AppDatabase.getInstance(this) }
}
