package com.example.jettodo.model

import com.example.jettodo.entities.TodoEntity

data class Todo(
    val id: Int,
    var title: String,
    var label: String,
    var priority: Priority,
    var isCompleted: Boolean = false
)

enum class Priority {
    LOW, MEDIUM, HIGH
}

// Extension functions
fun Todo.toEntity(): TodoEntity {
    return TodoEntity(
        id = this.id,
        title = this.title,
        label = this.label,
        priority = this.priority.name,
        isCompleted = this.isCompleted
    )
}

fun TodoEntity.toTodo(): Todo {
    return Todo(
        id = this.id,
        title = this.title,
        label = this.label,
        priority = Priority.valueOf(this.priority),
        isCompleted = this.isCompleted
    )
}
