package br.pucpr.appdev20241.checklistfolha.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import br.pucpr.appdev20241.checklistfolha.databinding.AdapterItemsBinding
import br.pucpr.appdev20241.checklistfolha.model.DataStore
import br.pucpr.appdev20241.checklistfolha.model.ToDo
import kotlinx.coroutines.launch

class ItemsAdapter (var todoItems: MutableList<ToDo>): RecyclerView.Adapter<ItemsAdapter.ItemHolder> ()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsAdapter.ItemHolder {
        AdapterItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false).run{
            return ItemHolder(this)
        }
    }

    override fun onBindViewHolder(holder: ItemsAdapter.ItemHolder, position: Int) {
        val currentItem = todoItems[position]
        holder.binding.itemName.text = currentItem.itemName
        holder.binding.itemStatus.isChecked = currentItem.itemStatus

            holder.binding.itemStatus.setOnCheckedChangeListener { _, isChecked ->
                currentItem.itemStatus = isChecked
                holder.itemView.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                    saveUpdatedToDoItem(position, currentItem)
                }
            }
    }

    fun updateData(newItems: MutableList<ToDo>) {
        todoItems = newItems
        notifyDataSetChanged()
    }

    private suspend fun saveUpdatedToDoItem(position: Int, updatedToDo: ToDo) {
        DataStore.editToDoItem(updatedToDo)
    }

    override fun getItemCount(): Int = todoItems.size

    inner class ItemHolder(val binding: AdapterItemsBinding): RecyclerView.ViewHolder(binding.root)
}