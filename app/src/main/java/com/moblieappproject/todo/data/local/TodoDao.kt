package com.moblieappproject.todo.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao// marks this as a data access object
interface TodoDao {
    // Returns a Flow that emits the full list whenever the table changes
    @Query("SELECT * FROM todos")
    fun getAllFlow(): Flow<List<TodoEntity>>

    // Returns a Flow for a single row by id
    @Query("SELECT * FROM todos WHERE id = :id")
    fun getByIdFlow(id: Int): Flow<TodoEntity?>

    // Inserts a list of todos, replacing on conflict (same id)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<TodoEntity>)
}
