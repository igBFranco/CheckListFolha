package br.pucpr.appdev20241.checklistfolha.view

import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.pucpr.appdev20241.checklistfolha.R
import br.pucpr.appdev20241.checklistfolha.databinding.FragmentCheckListBinding
import br.pucpr.appdev20241.checklistfolha.model.DataStore
import br.pucpr.appdev20241.checklistfolha.model.ToDo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CheckList : Fragment() {
    private var _binding: FragmentCheckListBinding? = null
    private lateinit var binding: FragmentCheckListBinding
    private lateinit var adapter: ItemsAdapter
    private lateinit var gesture: GestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentCheckListBinding.inflate(layoutInflater)
        configureRecycleViewEvent()
        configureGesture()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            loadRecyclerView()
            addItemConfig()
        }
    }

    private fun addItemConfig() {
        val fab: Button = binding.buttonAdd

        fab.setOnClickListener {
            val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.adapter_input_form, null)
            val dialogBuilder = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setTitle("Adicionar novo Item")
            val alertDialog = dialogBuilder.create()
            alertDialog.show()
            val editText = dialogView.findViewById<EditText>(R.id.editText)
            dialogView.findViewById<Button>(R.id.btnAddItem).setOnClickListener {
                val text = editText.text.toString()
                viewLifecycleOwner.lifecycleScope.launch {
                    DataStore.addToDoItem(ToDo(itemName = text, itemStatus = false))
                    val updatedList = DataStore.getAllToDos().toMutableList()
                    withContext(Dispatchers.Main) {
                        adapter.updateData(updatedList)
                        Toast.makeText(requireContext(), "Item adicionado com sucesso!", Toast.LENGTH_LONG).show()
                        alertDialog.dismiss()
                    }
                }
            }
        }
    }

    private suspend fun loadRecyclerView() {
        val todos = DataStore.getAllToDos().toMutableList()
        LinearLayoutManager(requireContext()).run {
            this.orientation = LinearLayoutManager.VERTICAL
            binding.rcvItems.layoutManager = this
            adapter = ItemsAdapter(todos)
            binding.rcvItems.adapter = adapter
        }
    }

    private fun configureGesture() {
        gesture = GestureDetector(requireContext(), object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                binding.rcvItems.findChildViewUnder(e.x, e.y)?.let { view ->
                    val position = binding.rcvItems.getChildAdapterPosition(view)
                    showEditDialog(position)
                }
                return true
            }

            override fun onLongPress(e: MotionEvent) {
                super.onLongPress(e)
                binding.rcvItems.findChildViewUnder(e.x, e.y).run {
                    this?.let { view ->
                        binding.rcvItems.getChildAdapterPosition(view).apply {
                            viewLifecycleOwner.lifecycleScope.launch {
                                val todoItem = DataStore.getToDoItem(this@apply)
                                AlertDialog.Builder(requireContext()).run {
                                    setMessage("Tem certeza que deseja remover este Item?")
                                    setNegativeButton("Cancelar") { _, _ -> }
                                    setPositiveButton("Excluir") { _, _ ->
                                        viewLifecycleOwner.lifecycleScope.launch {
                                            DataStore.deleteToDoItemByPosition(this@apply)
                                            val updatedList = DataStore.getAllToDos().toMutableList()
                                            withContext(Dispatchers.Main) {
                                                adapter.updateData(updatedList)
                                                Toast.makeText(requireContext(), "Item ${todoItem?.itemName} removido com sucesso!", Toast.LENGTH_LONG).show()
                                            }
                                        }
                                    }
                                }.show()
                            }
                        }
                    }
                }
            }
        })
    }

    private fun showEditDialog(position: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            val todoItem = DataStore.getToDoItem(position)
            Log.d("CheckList", "Retrieved item: $todoItem")
            val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.adapter_edit_form, null)
            val editText = dialogView.findViewById<EditText>(R.id.editText).apply {
                setText(todoItem?.itemName)
            }

            AlertDialog.Builder(requireContext()).run {
                setView(dialogView)
                setTitle("Editar Item")
                setPositiveButton("Salvar") { _, _ ->
                    val updatedText = editText.text.toString()
                    todoItem?.itemName = updatedText
                    if (todoItem != null) {
                        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                            DataStore.editToDoItem(position, todoItem)
                            val updatedList = DataStore.getAllToDos().toMutableList()
                            withContext(Dispatchers.Main) {
                                adapter.updateData(updatedList)
                                Toast.makeText(requireContext(), "Item editado com sucesso!", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
                setNegativeButton("Cancelar", null)
            }.show()
        }
    }

    private fun configureRecycleViewEvent() {
        binding.rcvItems.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                val child = rv.findChildViewUnder(e.x, e.y)
                if (child != null) {
                    val checkbox = child.findViewById<View>(R.id.itemStatus)
                    if (checkbox != null && e.action == MotionEvent.ACTION_DOWN && isPointInsideView(e.rawX, e.rawY, checkbox)) {
                        return false
                    }
                }
                return gesture.onTouchEvent(e)
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

        })
    }

    private fun isPointInsideView(x: Float, y: Float, view: View): Boolean {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val viewX = location[0]
        val viewY = location[1]
        return x >= viewX && x <= viewX + view.width && y >= viewY && y <= viewY + view.height
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
