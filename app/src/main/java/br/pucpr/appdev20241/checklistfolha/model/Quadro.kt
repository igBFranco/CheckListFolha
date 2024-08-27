package br.pucpr.appdev20241.checklistfolha.model

import com.google.firebase.firestore.DocumentId
import java.util.Date

 data class Quadro (
    @DocumentId val id: String = "",
    var quadroLocal: String = "",
    var dataEntrega: Date = Date(),
    var imageUrl: String? = null
) {
     constructor() : this("", "", Date())
 }