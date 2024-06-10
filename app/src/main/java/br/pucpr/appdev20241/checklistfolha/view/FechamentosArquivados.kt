package br.pucpr.appdev20241.checklistfolha.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import br.pucpr.appdev20241.checklistfolha.databinding.FragmentFechamentoArchivedBinding
import br.pucpr.appdev20241.checklistfolha.model.DataStore
import br.pucpr.appdev20241.checklistfolha.model.Fechamento
import kotlinx.coroutines.launch

class FechamentosArquivados: Fragment() {

    private var _binding: FragmentFechamentoArchivedBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: FechamentoArquivadoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFechamentoArchivedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadArchivedFechamentos()
    }

    private fun loadArchivedFechamentos() {
        viewLifecycleOwner.lifecycleScope.launch {
            val fechamentos = DataStore.getAllFechamentos()
            displayFechamentos(fechamentos)
        }
    }

    private fun displayFechamentos(fechamentos: List<Fechamento>) {
        binding.recyclerViewArquivo.layoutManager = LinearLayoutManager(context)
        adapter = FechamentoArquivadoAdapter(fechamentos)
        binding.recyclerViewArquivo.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}