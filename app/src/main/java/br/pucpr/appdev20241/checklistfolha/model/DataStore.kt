package br.pucpr.appdev20241.checklistfolha.model

import java.util.Date

object DataStore {

    var todoItems: MutableList<ToDo> = arrayListOf()
    var quadrosEntregues: MutableList<Quadro> = arrayListOf()

    init {
        addItem(ToDo("Fechar Folha", true))
        addItem(ToDo("Gerar Guia INSS", true))
        addItem(ToDo("Gerar Guia FGTS", false))

        addQuadro(Quadro("Prefeitura", Date()))
        addQuadro(Quadro("Educação", Date()))
    }

    fun getItem(position: Int): ToDo {
        return todoItems[position]
    }

    fun addItem(toDo: ToDo) {
        todoItems.add(toDo)
    }

    fun editItem(position: Int, toDo: ToDo) {
        todoItems[position] = toDo
    }

    fun removeItem(position: Int) {
        todoItems.removeAt(position)
    }

    fun getQuadro(position: Int): Quadro {
        return quadrosEntregues[position]
    }

    fun addQuadro(quadro: Quadro) {
        quadrosEntregues.add(quadro)
    }

    fun editQuadro(position: Int, quadro: Quadro) {
        quadrosEntregues[position] = quadro
    }

    fun removeQuadro(position: Int) {
        quadrosEntregues.removeAt(position)
    }
}