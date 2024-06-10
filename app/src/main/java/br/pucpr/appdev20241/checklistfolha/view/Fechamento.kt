package br.pucpr.appdev20241.checklistfolha.view

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFechamentoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSave.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                val areAllCompleted = DataStore.areAllItemsCompleted()
                if (areAllCompleted) {
                    val todos = DataStore.getAllToDos()
                    val quadros = DataStore.getAllQuadros()
                    val allItems = todos + quadros
                    displayItems(allItems)

                    val competencia = getCurrentCompetencia()
                    DataStore.saveFechamento(competencia)

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

    private fun getCurrentCompetencia(): String {
        val calendar = Calendar.getInstance()
        val month = calendar.get(Calendar.MONTH) + 1 // January is 0
        val year = calendar.get(Calendar.YEAR)
        return String.format("%02d/%d", month, year)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}