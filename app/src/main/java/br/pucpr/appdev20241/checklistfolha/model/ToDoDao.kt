package br.pucpr.appdev20241.checklistfolha.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete

@Dao
interface ToDoDao {
    @Query("SELECT * FROM ToDo")
    fun getAll(): List<ToDo>

    @Insert
    fun insert(toDo: ToDo)

    @Update
    fun update(toDo: ToDo)

    @Delete
    fun delete(toDo: ToDo)

    @Query("DELETE FROM ToDo")
    suspend fun deleteAll(): Int
}