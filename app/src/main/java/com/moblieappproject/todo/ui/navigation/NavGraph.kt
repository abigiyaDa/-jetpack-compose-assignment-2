package com.moblieappproject.todo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.moblieappproject.todo.ui.screen.TodoDetailScreen
import com.moblieappproject.todo.ui.screen.TodoListScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "list") {
        // “list” route shows the list screen
        composable("list") {
            TodoListScreen(navController)
        }
        // “detail/{id}” route shows the detail screen, passing the id in the back stack
        composable("detail/{id}") { backStack ->
            val id = backStack.arguments?.getString("id")?.toIntOrNull() ?: 0
            TodoDetailScreen(id, navController)
        }
    }
}
