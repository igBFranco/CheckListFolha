package br.pucpr.appdev20241.checklistfolha.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.pucpr.appdev20241.checklistfolha.R
import br.pucpr.appdev20241.checklistfolha.model.Quadro
import br.pucpr.appdev20241.checklistfolha.model.ToDo

class FechamentoAdapter(private val items: List<Any>) : RecyclerView.Adapter<FechamentoAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemName: TextView = itemView.findViewById(R.id.itemName)
        val itemDate: TextView = itemView.findViewById(R.id.itemDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_fechamento, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        if (item is ToDo) {
            holder.itemName.text = item.itemName
            holder.itemDate.text = "Status: ${item.itemStatus}"
        } else if (item is Quadro) {
            holder.itemName.text = item.quadroLocal
            holder.itemDate.text = "Data: ${item.dataEntrega}"
        }
    }

    override fun getItemCount() = items.size
}