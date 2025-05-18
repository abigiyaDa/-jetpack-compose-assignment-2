package com.moblieappproject.todo.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TodoEntity::class], version = 1)
abstract class TodoDatabase: RoomDatabase() {
    abstract val todoDao: TodoDao

    companion object {
        @Volatile private var INSTANCE: TodoDatabase? = null

        // Returns the singleton database instance
        fun getInstance(context: Context): TodoDatabase =
            INSTANCE ?: synchronized(this) {

                Room.databaseBuilder(
                    context.applicationContext,
                    TodoDatabase::class.java,
                    "todo_db"
                )
                    .build()
                    .also { INSTANCE = it }
            }
    }
}
