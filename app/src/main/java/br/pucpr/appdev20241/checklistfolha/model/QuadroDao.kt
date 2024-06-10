package br.pucpr.appdev20241.checklistfolha.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete

@Dao
interface QuadroDao {
    @Query("SELECT * FROM Quadro")
    fun getAll(): List<Quadro>

    @Insert
    fun insert(quadro: Quadro)

    @Update
    fun update(quadro: Quadro)

    @Delete
    fun delete(quadro: Quadro)

    @Query("DELETE FROM Quadro")
    suspend fun deleteAll(): Int
}