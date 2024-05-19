package br.pucpr.appdev20241.checklistfolha.model

class ToDo (
    var itemName: String,
    var itemStatus: Boolean
) {
    constructor(): this("", false)
    constructor(itemName: String): this(itemName, false)
}