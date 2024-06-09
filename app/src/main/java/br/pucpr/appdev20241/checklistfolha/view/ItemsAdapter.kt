package br.pucpr.appdev20241.checklistfolha.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.pucpr.appdev20241.checklistfolha.databinding.AdapterItemsBinding
import br.pucpr.appdev20241.checklistfolha.model.ToDo

class ItemsAdapter (var todoItems: MutableList<ToDo>): RecyclerView.Adapter<ItemsAdapter.ItemHolder> ()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsAdapter.ItemHolder {
        AdapterItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false).run{
            return ItemHolder(this)
        }
    }

    override fun onBindViewHolder(holder: ItemsAdapter.ItemHolder, position: Int) {
        todoItems[position].run {
            holder.binding.itemName.text = this.itemName
            holder.binding.itemStatus.isChecked = this.itemStatus

            holder.binding.itemStatus.setOnCheckedChangeListener { _, isChecked ->
                this.itemStatus = isChecked
            }
        }
    }

    fun updateData(newItems: MutableList<ToDo>) {
        todoItems = newItems
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = todoItems.size

    inner class ItemHolder(val binding: AdapterItemsBinding): RecyclerView.ViewHolder(binding.root)
}