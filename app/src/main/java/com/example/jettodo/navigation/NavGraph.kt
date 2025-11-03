package com.example.jettodo.navigation

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.jettodo.model.Todo
import com.example.jettodo.TodoViewModel
import com.example.jettodo.ui.screens.AddEditTodoScreen
import com.example.jettodo.ui.screens.TodoListScreen

sealed class Screen(val route: String) {
    object TodoList : Screen("todo_list")
    object AddTodo : Screen("add_todo")
    object EditTodo : Screen("edit_todo/{todoId}") {
        fun createRoute(todoId: Int) = "edit_todo/$todoId"
    }
}

@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: TodoViewModel = viewModel()  // ← LE VIEWMODEL EST CRÉÉ ICI
) {
    // Observer les todos depuis le ViewModel
    val todos by viewModel.todos.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Screen.TodoList.route
    ) {
        // ÉCRAN: Liste des Todos
        composable(Screen.TodoList.route) {
            TodoListScreen(
                todos = todos,  // ← Données du ViewModel
                onTodoClick = { todoId ->
                    navController.navigate(Screen.EditTodo.createRoute(todoId))
                },
                onAddClick = {
                    navController.navigate(Screen.AddTodo.route)
                },
                onToggleComplete = { todoId ->
                    viewModel.toggleComplete(todoId)  // ← Appel ViewModel
                },
                onDeleteTodo = { todoId ->
                    viewModel.deleteTodo(todoId)  // ← Appel ViewModel
                }
            )
        }

        composable(Screen.AddTodo.route) {
            AddEditTodoScreen(
                todoId = null,
                onSave = { title, label, priority ->
                    viewModel.addTodo(title, label, priority)  // viewModel.addTodo accepte Priority
                    navController.popBackStack()
                },
                onClose = {
                    navController.popBackStack()
                }
            )
        }

        // ÉCRAN: Modifier un Todo
        composable(
            route = Screen.EditTodo.route,
            arguments = listOf(
                navArgument("todoId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val todoId = backStackEntry.arguments?.getInt("todoId") ?: return@composable

            var todo by remember { mutableStateOf<Todo?>(null) }

            LaunchedEffect(todoId) {
                todo = viewModel.getTodoById(todoId)  // ← Appel ViewModel
            }

            todo?.let { currentTodo ->
                AddEditTodoScreen(
                    todoId = todoId,
                    initialTitle = currentTodo.title,
                    initialLabel = currentTodo.label,
                    initialPriority = currentTodo.priority,
                    onSave = { title, label, priority ->
                        val updatedTodo = currentTodo.copy(
                            title = title,
                            label = label,
                            priority = priority
                        )
                        viewModel.updateTodo(updatedTodo)  // ← Appel ViewModel
                        navController.popBackStack()
                    },
                    onClose = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}