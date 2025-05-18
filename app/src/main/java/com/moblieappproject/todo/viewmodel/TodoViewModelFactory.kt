package com.moblieappproject.todo.viewmodel

import com.moblieappproject.todo.data.local.TodoDatabase
import com.moblieappproject.todo.data.network.RetrofitClient
import com.moblieappproject.todo.repository.TodoRepository
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory {
    //for list screen
    class ListFactory(context: Context): ViewModelProvider.Factory {
        private val repo = TodoRepository(
            RetrofitClient.apiService,
            TodoDatabase.getInstance(context).todoDao
        )
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return TodoListViewModel(repo) as T
        }
    }

    class DetailFactory(context: Context, private val todoId: Int): ViewModelProvider.Factory {
        private val repo = TodoRepository(
            RetrofitClient.apiService,
            TodoDatabase.getInstance(context).todoDao
        )
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return TodoDetailViewModel(repo, todoId) as T
        }
    }
}

