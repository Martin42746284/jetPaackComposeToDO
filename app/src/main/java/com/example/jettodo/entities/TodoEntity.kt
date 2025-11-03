package com.example.jettodo.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todos")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val label: String,
    val priority: String,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

