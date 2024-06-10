package br.pucpr.appdev20241.checklistfolha.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
@Dao
interface FechamentoDao {
    @Insert
    suspend fun insert(fechamento: Fechamento)

    @Query("SELECT * FROM Fechamento")
    suspend fun getAllFechamentos(): List<Fechamento>

    @Query("SELECT * FROM Fechamento WHERE competencia = :competencia")
    suspend fun getByCompetencia(competencia: String): Fechamento?

    @Query("DELETE FROM Fechamento WHERE id = :id")
    suspend fun deleteById(id: Int)
}