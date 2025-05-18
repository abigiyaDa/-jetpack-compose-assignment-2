package com.moblieappproject.todo.data.model

data class Todo(
    val userId: Int,
    val id: Int,
    val title: String,
    val completed: Boolean
)
//data class Todo: holds exactly the four fields returned by the API.
//Whenever Retrofit parses JSON, it will instantiate this class directly.