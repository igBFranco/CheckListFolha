package br.pucpr.appdev20241.checklistfolha.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.pucpr.appdev20241.checklistfolha.databinding.AdapterQuadrosBinding
import br.pucpr.appdev20241.checklistfolha.model.Quadro
import java.text.SimpleDateFormat
import java.util.Locale

class QuadrosAdapter (var quadrosEntregues: MutableList<Quadro>): RecyclerView.Adapter<QuadrosAdapter.QuadroHolder> (){
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuadrosAdapter.QuadroHolder {
        AdapterQuadrosBinding.inflate(LayoutInflater.from(parent.context), parent, false).run{
            return QuadroHolder(this)
        }
    }

    override fun onBindViewHolder(holder: QuadrosAdapter.QuadroHolder, position: Int) {
        quadrosEntregues[position].run {
            holder.binding.quadroLocal.text = this.quadroLocal
            holder.binding.dataEntrega.text = dateFormat.format(this.dataEntrega)

        }
    }

    override fun getItemCount(): Int = quadrosEntregues.size

    inner class QuadroHolder(val binding: AdapterQuadrosBinding): RecyclerView.ViewHolder(binding.root)

}