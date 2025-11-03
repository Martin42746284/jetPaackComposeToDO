package com.example.jettodo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.jettodo.config.TodoDatabase
import com.example.jettodo.repositories.TodoRepository
import com.example.jettodo.model.Priority
import com.example.jettodo.model.Todo
import com.example.jettodo.model.toEntity
import com.example.jettodo.model.toTodo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TodoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TodoRepository

    private val _todos = MutableStateFlow<List<Todo>>(emptyList())
    val todos: StateFlow<List<Todo>> = _todos.asStateFlow()

    init {
        val database = TodoDatabase.getDatabase(application)
        repository = TodoRepository(database.todoDao())

        viewModelScope.launch {
            repository.allTodos.collect { entities ->
                _todos.value = entities.map { it.toTodo() }
            }
        }
    }

    fun addTodo(title: String, label: String, priority: Priority) {
        viewModelScope.launch {
            val todo = Todo(0, title, label, priority, false)
            repository.insert(todo.toEntity())
        }
    }

    fun updateTodo(todo: Todo) {
        viewModelScope.launch {
            repository.update(todo.toEntity())
        }
    }

    fun deleteTodo(id: Int) {
        viewModelScope.launch {
            repository.delete(id)
        }
    }

    fun toggleComplete(id: Int) {
        viewModelScope.launch {
            val todo = repository.getTodoById(id)
            todo?.let {
                repository.toggleComplete(id, !it.isCompleted)
            }
        }
    }

    suspend fun getTodoById(id: Int): Todo? {
        return repository.getTodoById(id)?.toTodo()
    }
}