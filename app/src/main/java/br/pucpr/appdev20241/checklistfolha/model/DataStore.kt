package br.pucpr.appdev20241.checklistfolha.model

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
object DataStore {
//    var todoItems: MutableList<ToDo> = arrayListOf()
//    var quadrosEntregues: MutableList<Quadro> = arrayListOf()

    private lateinit var db: AppDataBase
    fun init(context: Context) {
      db = AppDataBase.getDatabase(context)
    }

    suspend fun getToDoItem(position: Int): ToDo? {
        val todos = getAllToDos()
        return if (position in todos.indices) todos[position] else null
    }
    suspend fun getAllToDos(): List<ToDo> = withContext(Dispatchers.IO) {
        db.toDoDao().getAll()
    }

    suspend fun addToDoItem(toDo: ToDo) = withContext(Dispatchers.IO) {
        db.toDoDao().insert(toDo)
    }

    suspend fun editToDoItem(position: Int, toDo: ToDo) {
        withContext(Dispatchers.IO) {
            db.toDoDao().update(toDo)
        }
    }

    suspend fun deleteToDoItem(toDo: ToDo) = withContext(Dispatchers.IO) {
        db.toDoDao().delete(toDo)
    }

    suspend fun deleteToDoItemByPosition(position: Int) {
        val todos = getAllToDos()
        if (position in todos.indices) {
            deleteToDoItem(todos[position])
        }
    }

    suspend fun getQuadroItem(position: Int): Quadro? {
        val quadros = getAllQuadros()
        return if (position in quadros.indices) quadros[position] else null
    }
    suspend fun getAllQuadros(): List<Quadro> = withContext(Dispatchers.IO) {
        db.quadroDao().getAll()
    }

    suspend fun addQuadroItem(quadro: Quadro) = withContext(Dispatchers.IO) {
        db.quadroDao().insert(quadro)
    }

    suspend fun editQuadroItem(position: Int, quadro: Quadro) {
        withContext(Dispatchers.IO) {
            db.quadroDao().update(quadro)
        }
    }

    suspend fun deleteQuadroItem(quadro: Quadro) = withContext(Dispatchers.IO) {
        db.quadroDao().delete(quadro)
    }

    suspend fun deleteQuadroItemByPosition(position: Int) {
        val quadros = getAllQuadros()
        if (position in quadros.indices) {
            deleteQuadroItem(quadros[position])
        }
    }

//    fun getItem(position: Int): ToDo {
//        return todoItems[position]
//    }
//
//    fun addItem(toDo: ToDo) {
//        todoItems.add(toDo)
//    }
//
//    fun editItem(position: Int, toDo: ToDo) {
//        todoItems[position] = toDo
//    }
//
//    fun removeItem(position: Int) {
//        todoItems.removeAt(position)
//    }
//
//    fun getQuadro(position: Int): Quadro {
//        return quadrosEntregues[position]
//    }
//
//    fun addQuadro(quadro: Quadro) {
//        quadrosEntregues.add(quadro)
//    }
//
//    fun editQuadro(position: Int, quadro: Quadro) {
//        quadrosEntregues[position] = quadro
//    }
//
//    fun removeQuadro(position: Int) {
//        quadrosEntregues.removeAt(position)
//    }
}