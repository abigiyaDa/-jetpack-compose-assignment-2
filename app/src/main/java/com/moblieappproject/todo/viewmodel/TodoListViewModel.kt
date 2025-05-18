package com.moblieappproject.todo.viewmodel

import com.moblieappproject.todo.data.model.Todo
import com.moblieappproject.todo.repository.TodoRepository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// define ui state
sealed class TodoListUiState {
    object Loading : TodoListUiState()
    data class Success(val todos: List<Todo>) : TodoListUiState()
    data class Error(val message: String, val cached: List<Todo>) : TodoListUiState()
}

class TodoListViewModel(private val repo: TodoRepository) : ViewModel() {
    // _uiState holds internal state
    private val _uiState = MutableStateFlow<TodoListUiState>(TodoListUiState.Loading)
    // uiState read only , exposed to ui
    val uiState: StateFlow<TodoListUiState> = _uiState

    init { fetch() }

    fun fetch() {
        viewModelScope.launch {
            repo.getAllTodos().collect { result ->
                result.fold(
                    onSuccess = { list ->
                        _uiState.value = TodoListUiState.Success(list)
                    },
                    onFailure = { err ->
                        //preserve any previously loaded cache
                        val cache = (uiState.value as? TodoListUiState.Success)?.todos ?: emptyList()
                        _uiState.value = TodoListUiState.Error(err.message ?: "Unknown error", cache)
                    }
                )
            }
        }
    }

    fun retry() = fetch()
}

