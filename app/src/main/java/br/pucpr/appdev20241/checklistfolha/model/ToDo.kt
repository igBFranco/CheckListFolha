package br.pucpr.appdev20241.checklistfolha.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId

data class ToDo (
    @DocumentId val id: String = "",
    var itemName: String,
    var itemStatus: Boolean
) {
    constructor() : this("", "", false)
}