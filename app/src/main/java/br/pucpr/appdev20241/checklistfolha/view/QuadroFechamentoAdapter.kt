package br.pucpr.appdev20241.checklistfolha.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.pucpr.appdev20241.checklistfolha.R
import br.pucpr.appdev20241.checklistfolha.model.Quadro
import java.text.SimpleDateFormat
import java.util.Locale

class QuadroFechamentoAdapter(
    private val quadros: List<Quadro>
) : RecyclerView.Adapter<QuadroFechamentoAdapter.QuadroViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuadroViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_fechamento, parent, false)
        return QuadroViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuadroViewHolder, position: Int) {
        val quadro = quadros[position]
        holder.bind(quadro)
    }

    override fun getItemCount(): Int = quadros.size

    class QuadroViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        private val textName: TextView = itemView.findViewById(R.id.itemName)
        private val textDataEntrega: TextView = itemView.findViewById(R.id.itemDate)

        fun bind(quadro: Quadro) {
            textName.text = quadro.quadroLocal
            textDataEntrega.text = dateFormat.format(quadro.dataEntrega)
        }
    }
}
