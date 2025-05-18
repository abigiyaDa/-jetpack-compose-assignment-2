package com.moblieappproject.todo.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.moblieappproject.todo.viewmodel.TodoDetailUiState
import com.moblieappproject.todo.viewmodel.TodoDetailViewModel
import com.moblieappproject.todo.viewmodel.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoDetailScreen(todoId: Int, navController: NavController) {
    val context = LocalContext.current
    //Initialize ViewModel
    val vm: TodoDetailViewModel = viewModel(
        factory = ViewModelFactory.DetailFactory(context, todoId)
    )
    // collect ui state
    val state by vm.uiState.collectAsState(initial = TodoDetailUiState.Loading)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Todo Detail") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.TopStart
        ) {
            when (state) {
                is TodoDetailUiState.Loading -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                    ) { CircularProgressIndicator() }
                is TodoDetailUiState.Error -> Text(
                    text = (state as TodoDetailUiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
                is TodoDetailUiState.Success -> {
                    val todo = (state as TodoDetailUiState.Success).todo

                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("ID: ${todo.id}", style = MaterialTheme.typography.titleLarge)
                        Text("User ID: ${todo.userId}", style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(todo.title, style = MaterialTheme.typography.headlineSmall)
                        Spacer(modifier = Modifier.height(8.dp))
                        //
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            val icon = if (todo.completed) Icons.Filled.CheckCircle else Icons.Filled.DateRange
                            val iconColor = if (todo.completed) Color(0xFF4CAF50) else Color(0xFFFFC107)
                            val statusText = if (todo.completed) "Completed" else "Pending"
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = iconColor,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = statusText,
                                style = MaterialTheme.typography.titleLarge,
                                color = iconColor
                            )
                        }
                    }
                }
            }
        }
    }
}
