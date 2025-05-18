package com.moblieappproject.todo.viewmodel

import com.moblieappproject.todo.data.model.Todo
import com.moblieappproject.todo.repository.TodoRepository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class TodoDetailUiState {
    object Loading : TodoDetailUiState()
    data class Success(val todo: Todo) : TodoDetailUiState()
    data class Error(val message: String) : TodoDetailUiState()
}

class TodoDetailViewModel(private val repo: TodoRepository, private val todoId: Int): ViewModel() {
    private val _uiState = MutableStateFlow<TodoDetailUiState>(TodoDetailUiState.Loading)
    val uiState: StateFlow<TodoDetailUiState> = _uiState//.asStateFlow()

    init { loadDetail() }

    private fun loadDetail() {
        viewModelScope.launch {
            repo.getTodoById(todoId).collect { todo ->
                if (todo != null) {
                    _uiState.value = TodoDetailUiState.Success(todo)
                } else {
                    _uiState.value = TodoDetailUiState.Error("Todo not found")
                }
            }
        }
    }
}
