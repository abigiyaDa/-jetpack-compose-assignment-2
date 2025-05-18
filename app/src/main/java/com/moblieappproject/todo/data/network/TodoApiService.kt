package com.moblieappproject.todo.data.network


import com.moblieappproject.todo.data.model.Todo
import retrofit2.Response
import retrofit2.http.GET


interface TodoApiService {
    @GET("todos")// get request to the end point "todos"
    suspend fun fetchTodos(): Response<List<Todo>>
}
//suspend fun fetchTodos(): a Kotlin coroutine function returning a Response wrapping a List<Todo>