package br.pucpr.appdev20241.checklistfolha.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.pucpr.appdev20241.checklistfolha.R
import br.pucpr.appdev20241.checklistfolha.model.ToDo

class ToDoAdapter(
    private val todos: List<ToDo>
) : RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_fechamento, parent, false)
        return ToDoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        val todo = todos[position]
        holder.bind(todo)
    }

    override fun getItemCount(): Int = todos.size

    class ToDoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textTitle: TextView = itemView.findViewById(R.id.itemName)
        private val textStatus: TextView = itemView.findViewById(R.id.itemDate)

        fun bind(todo: ToDo) {
            textTitle.text = todo.itemName
            textStatus.text = if (todo.itemStatus) "Concluído" else "Pendente"
        }
    }
}
