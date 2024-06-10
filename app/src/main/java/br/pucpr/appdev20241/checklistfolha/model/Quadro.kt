package br.pucpr.appdev20241.checklistfolha.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "Quadro")
 data class Quadro (
     @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var quadroLocal: String,
    var dataEntrega: Date
) {}