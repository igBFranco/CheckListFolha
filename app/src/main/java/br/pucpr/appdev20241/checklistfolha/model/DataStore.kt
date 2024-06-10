package br.pucpr.appdev20241.checklistfolha.model

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
object DataStore {

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
    suspend fun areAllItemsCompleted(): Boolean = withContext(Dispatchers.IO) {
        val todos = db.toDoDao().getAll()
        todos.all { it.itemStatus }
    }

    suspend fun saveFechamento(competencia: String) = withContext(Dispatchers.IO) {
        val todos = getAllToDos()
        val quadros = getAllQuadros()
        val fechamento = Fechamento(
            competencia = competencia,
            todos = todos,
            quadros = quadros
        )
        db.fechamentoDao().insert(fechamento)
        clearData()
    }

    suspend fun getAllFechamentos(): List<Fechamento> {
        return db.fechamentoDao().getAllFechamentos()
    }

    suspend fun getFechamentoByCompetencia(competencia: String): Fechamento? = withContext(Dispatchers.IO) {
        db.fechamentoDao().getByCompetencia(competencia)
    }

    suspend fun clearData() = withContext(Dispatchers.IO) {
        val deletedToDos = db.toDoDao().deleteAll()
        val deletedQuadros = db.quadroDao().deleteAll()
        Log.d("DataStore", "Deleted $deletedToDos ToDos and $deletedQuadros Quadros")

    }
}