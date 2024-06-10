package br.pucpr.appdev20241.checklistfolha.model

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class Fechamento(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val competencia: String,
    val todos: List<ToDo>,
    val quadros: List<Quadro>
)
