package br.pucpr.appdev20241.checklistfolha.view

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import br.pucpr.appdev20241.checklistfolha.R
import br.pucpr.appdev20241.checklistfolha.databinding.FragmentCheckListBinding
import br.pucpr.appdev20241.checklistfolha.databinding.FragmentControleQuadrosBinding
import br.pucpr.appdev20241.checklistfolha.databinding.FragmentFechamentoBinding
import br.pucpr.appdev20241.checklistfolha.model.DataStore
import kotlinx.coroutines.launch
import java.util.Calendar

class Fechamento : Fragment() {

    private var _binding: FragmentFechamentoBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: FechamentoAdapter
    private var selectedCompetencia: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFechamentoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateCounts()

        binding.buttonSelectCompetencia.setOnClickListener {
            showDatePickerDialog()
        }

        binding.buttonSave.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                val areAllCompleted = DataStore.areAllItemsCompleted()
                if (areAllCompleted) {
                    if (selectedCompetencia == null) {
                        Toast.makeText(requireContext(), "Por favor, selecione uma competência.", Toast.LENGTH_LONG).show()
                        return@launch
                    }
                    val todos = DataStore.getAllToDos()
                    val quadros = DataStore.getAllQuadros()
                    val allItems = todos + quadros
                    displayItems(allItems)

                    DataStore.saveFechamento(selectedCompetencia!!)

                    DataStore.clearData()
                    Toast.makeText(requireContext(), "Dados salvos com sucesso.", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireContext(), "Todos os itens devem estar concluídos para salvar.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun displayItems(items: List<Any>) {
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = FechamentoAdapter(items)
        binding.recyclerView.adapter = adapter
    }

    private fun updateCounts() {
        viewLifecycleOwner.lifecycleScope.launch {
            val todos = DataStore.getAllToDos()
            val quadros = DataStore.getAllQuadros()

            val totalTodos = todos.size
            val totalQuadros = quadros.size

            val totalTodosConcluidos = todos.count { it.itemStatus }
            val totalQuadrosEntregues = quadros.count { true }

            binding.itensOk.text = "$totalTodosConcluidos/$totalTodos"
            binding.quadrosOk.text = "$totalQuadrosEntregues/$totalQuadros"
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, _ ->
                selectedCompetencia = String.format("%02d/%d", selectedMonth + 1, selectedYear)
                Toast.makeText(requireContext(), "Competência selecionada: $selectedCompetencia", Toast.LENGTH_SHORT).show()
            },
            year,
            month,
            1
        )
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis() // Limitar seleção de datas futuras
        datePickerDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}