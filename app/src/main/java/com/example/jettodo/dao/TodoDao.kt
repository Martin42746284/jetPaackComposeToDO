package com.example.jettodo.dao

import androidx.room.*
import com.example.jettodo.entities.TodoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Query("SELECT * FROM todos ORDER BY createdAt DESC")
    fun getAllTodos(): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todos WHERE id = :id")
    suspend fun getTodoById(id: Int): TodoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todo: TodoEntity): Long

    @Update
    suspend fun updateTodo(todo: TodoEntity)

    @Query("DELETE FROM todos WHERE id = :id")
    suspend fun deleteTodoById(id: Int)

    @Query("UPDATE todos SET isCompleted = :isCompleted WHERE id = :id")
    suspend fun toggleTodoComplete(id: Int, isCompleted: Boolean)
}

