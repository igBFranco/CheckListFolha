package br.pucpr.appdev20241.checklistfolha.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.pucpr.appdev20241.checklistfolha.R
import br.pucpr.appdev20241.checklistfolha.model.Fechamento

class FechamentoArquivadoAdapter(
    private val fechamentos: List<Fechamento>
) : RecyclerView.Adapter<FechamentoArquivadoAdapter.FechamentoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FechamentoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_fechamento_arquivado, parent, false)
        return FechamentoViewHolder(view)
    }

    override fun onBindViewHolder(holder: FechamentoViewHolder, position: Int) {
        val fechamento = fechamentos[position]
        holder.bind(fechamento)
    }

    override fun getItemCount(): Int = fechamentos.size

    class FechamentoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textCompetencia: TextView = itemView.findViewById(R.id.textCompetencia)
        private val recyclerTodos: RecyclerView = itemView.findViewById(R.id.recyclerTodos)
        private val recyclerQuadros: RecyclerView = itemView.findViewById(R.id.recyclerQuadros)

        fun bind(fechamento: Fechamento) {
            textCompetencia.text = fechamento.competencia

            recyclerTodos.layoutManager = LinearLayoutManager(itemView.context)
            recyclerTodos.adapter = ToDoAdapter(fechamento.todos)

            recyclerQuadros.layoutManager = LinearLayoutManager(itemView.context)
            recyclerQuadros.adapter = QuadroFechamentoAdapter(fechamento.quadros)
        }
    }
}
