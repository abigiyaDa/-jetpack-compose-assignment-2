// TodoRepository.kt
package com.moblieappproject.todo.repository

import com.moblieappproject.todo.data.local.TodoDao
import com.moblieappproject.todo.data.local.TodoEntity
import com.moblieappproject.todo.data.model.Todo
import com.moblieappproject.todo.data.network.TodoApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.io.IOException

class TodoRepository(
    private val api: TodoApiService,
    private val dao: TodoDao
) {
    /**
     * Emits cached list, then attempts network refresh.
     * On success: updates cache and emits fresh list.
     * On error: emits a failure, but cached flows have already emitted.
     */
    fun getAllTodos(): Flow<Result<List<Todo>>> = flow {
        // 1. Cache first
        dao.getAllFlow()
            .map { list -> list.map { it.toModel() } }
            .collect { emit(Result.success(it)) }

        // 2. network Refresh or retrofit call
        try {
            //network call
            val resp = api.fetchTodos()

            if (resp.isSuccessful) {
                resp.body()?.let { todos ->
                    // map to entity and save in DB
                    val entities = todos.map {
                        TodoEntity(it.id, it.userId, it.title, it.completed)
                    }
                    // insert it into room
                    dao.insertAll(entities)
                    // and emit fresh list
                    emit(Result.success(todos))
                }
            } else {
                //HTTP error
                emit(Result.failure(IOException("HTTP ${resp.code()}")))
            }
        } catch (e: IOException) {
            // network or parsing error
            emit(Result.failure(e))
        }
    }.catch { emit(Result.failure(it)) }//catches unexpected exceptions

    //Emits cached todo
    fun getTodoById(id: Int): Flow<Todo?> =
        dao.getByIdFlow(id)
            .map { it?.toModel() }
            .catch { emit(null) }
}


