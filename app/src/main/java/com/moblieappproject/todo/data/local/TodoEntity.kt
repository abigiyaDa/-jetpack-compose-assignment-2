package com.moblieappproject.todo.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.moblieappproject.todo.data.model.Todo

@Entity(tableName = "todos")//marks this as a room table
data class TodoEntity(
    @PrimaryKey val id: Int, //primary ket column
    val userId: Int,
    val title: String,
    val completed: Boolean
){
    // convert this DB row into the domain model
    fun toModel() = Todo(
        userId = userId,
        id = id,
        title = title,
        completed = completed
    )
}
