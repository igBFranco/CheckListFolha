package br.pucpr.appdev20241.checklistfolha.model

import java.util.Date

class Quadro (
    var quadroLocal: String,
    var dataEntrega: Date
) {
    constructor(): this("", Date())
    constructor(quadroLocal: String): this(quadroLocal, Date())
}