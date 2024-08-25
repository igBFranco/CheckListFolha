package br.pucpr.appdev20241.checklistfolha.model

import com.google.firebase.firestore.DocumentId


data class Fechamento(
    @DocumentId val id: String = "",
    val competencia: String = "",
    val todos: List<ToDo> = emptyList(),
    val quadros: List<Quadro> = emptyList()
){
    constructor() : this("", "", emptyList(), emptyList())
}
