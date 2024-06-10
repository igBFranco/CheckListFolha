package br.pucpr.appdev20241.checklistfolha.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ToDo")
data class ToDo (
     @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var itemName: String,
    var itemStatus: Boolean
) {}