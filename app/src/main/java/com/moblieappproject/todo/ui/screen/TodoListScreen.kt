package com.moblieappproject.todo.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.moblieappproject.todo.ui.components.TodoItemCard
import com.moblieappproject.todo.viewmodel.TodoDetailUiState
import com.moblieappproject.todo.viewmodel.TodoListUiState
import com.moblieappproject.todo.viewmodel.TodoListViewModel
import com.moblieappproject.todo.viewmodel.ViewModelFactory
//collectAsState: converts StateFlow to Compose state
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(navController: NavController) {
    val context = LocalContext.current
    //Initialize ViewModel
    val vm: TodoListViewModel = viewModel(
        factory = ViewModelFactory.ListFactory(context)
    )
    // collect ui state
    val state by vm.uiState.collectAsState(initial = TodoDetailUiState.Loading)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Todo List") }
            )
        }) { padding ->
        when (state) {
            is TodoListUiState.Loading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }

            is TodoListUiState.Success -> {
                val todos = (state as TodoListUiState.Success).todos
                LazyColumn(modifier = Modifier
                    .fillMaxSize()
                    .padding(padding).padding(16.dp)
                ) { items(todos) { t ->
                    TodoItemCard(todo = t) {
                        navController.navigate("detail/${t.id}") }
                }}
            }
            is TodoListUiState.Error -> {
                val err = state as TodoListUiState.Error
                Column(Modifier.fillMaxSize().padding(padding)) {
                    Text("Error: ${err.message}", color = Color.Red)
                    Spacer(Modifier.height(8.dp))
                    Text("Showing cached data:", style = MaterialTheme.typography.titleSmall)
                    err.cached.forEach { t -> Text("• ${t.title}") }
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = { vm.retry() }) { Text("Retry") }
                }
            }
        }
    }
}


