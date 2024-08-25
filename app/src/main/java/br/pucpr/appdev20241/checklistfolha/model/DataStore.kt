package br.pucpr.appdev20241.checklistfolha.model

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

object DataStore {

    //private lateinit var db: AppDataBase
    private val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

    /*
    fun init(context: Context) {
      db = AppDataBase.getDatabase(context)
    }*/

    suspend fun getToDoItem(position: Int): ToDo? {
        val todos = getAllToDos()
        return if (position in todos.indices) todos[position] else null
    }
    suspend fun getAllToDos(): List<ToDo> = withContext(Dispatchers.IO) {
        val uid = auth.currentUser?.uid ?: return@withContext emptyList<ToDo>()
        val result = db.collection("users").document(uid).collection("todos").get().await()
        return@withContext result.toObjects(ToDo::class.java)
    }
    suspend fun addToDoItem(toDo: ToDo) = withContext(Dispatchers.IO) {
        val uid = auth.currentUser?.uid ?: return@withContext
        db.collection("users").document(uid).collection("todos").add(toDo)
    }
    suspend fun editToDoItem( toDo: ToDo) = withContext(Dispatchers.IO){
        val uid = auth.currentUser?.uid ?: return@withContext
        val docRef = db.collection("users").document(uid).collection("todos").document(toDo.id)
        docRef.set(toDo)

    }
    suspend fun deleteToDoItem(toDo: ToDo) = withContext(Dispatchers.IO) {
        val uid = auth.currentUser?.uid ?: return@withContext
        val docRef = db.collection("users").document(uid).collection("todos").document(toDo.id)
        docRef.delete()
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
        val uid = auth.currentUser?.uid ?: return@withContext emptyList<Quadro>()
        val result = db.collection("users").document(uid).collection("quadros").get().await()
        return@withContext result.toObjects(Quadro::class.java)
    }
    suspend fun addQuadroItem(quadro: Quadro) = withContext(Dispatchers.IO) {
        val uid = auth.currentUser?.uid ?: return@withContext
        db.collection("users").document(uid).collection("quadros").add(quadro)
    }
    suspend fun editQuadroItem( quadro: Quadro) = withContext(Dispatchers.IO){
        val uid = auth.currentUser?.uid ?: return@withContext
        val docRef = db.collection("users").document(uid).collection("quadros").document(quadro.id)
        docRef.set(quadro)
    }
    suspend fun deleteQuadroItem(quadro: Quadro) = withContext(Dispatchers.IO) {
        val uid = auth.currentUser?.uid ?: return@withContext
        val docRef = db.collection("users").document(uid).collection("quadros").document(quadro.id)
        docRef.delete()
    }
    suspend fun deleteQuadroItemByPosition(position: Int) {
        val quadros = getAllQuadros()
        if (position in quadros.indices) {
            deleteQuadroItem(quadros[position])
        }
    }
    suspend fun areAllItemsCompleted(): Boolean = withContext(Dispatchers.IO) {
        val todos = getAllToDos()
        todos.all { it.itemStatus }
    }

    suspend fun saveFechamento(competencia: String) = withContext(Dispatchers.IO) {
        val uid = auth.currentUser?.uid ?: return@withContext
        val todos = getAllToDos()
        val quadros = getAllQuadros()
        val fechamento = Fechamento(
            competencia = competencia,
            todos = todos,
            quadros = quadros
        )
        db.collection("users").document(uid).collection("fechamentos").add(fechamento)
        clearData()
    }

    suspend fun getAllFechamentos(): List<Fechamento> = withContext(Dispatchers.IO){
        val uid = auth.currentUser?.uid ?: return@withContext emptyList<Fechamento>()
        val result = db.collection("users").document(uid).collection("fechamentos").get().await()
        return@withContext result.toObjects(Fechamento::class.java)
    }

//    suspend fun getFechamentoByCompetencia(competencia: String): Fechamento? = withContext(Dispatchers.IO) {
//        db.fechamentoDao().getByCompetencia(competencia)
//    }

    suspend fun clearData() = withContext(Dispatchers.IO) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@withContext
        val todosQuerySnapshot = db.collection("users").document(uid).collection("todos").get().await()
        for (document in todosQuerySnapshot.documents) {
            document.reference.delete().await()
        }
        val quadrosQuerySnapshot = db.collection("users").document(uid).collection("quadros").get().await()
        for (document in quadrosQuerySnapshot.documents) {
            document.reference.delete().await()
        }
        Log.d("DataStore", "Deleted ${todosQuerySnapshot.size()} ToDos and ${quadrosQuerySnapshot.size()} Quadros")
    }
}