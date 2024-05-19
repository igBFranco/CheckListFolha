package br.pucpr.appdev20241.checklistfolha.view

import android.os.Bundle
import android.view.GestureDetector
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.pucpr.appdev20241.checklistfolha.R
import br.pucpr.appdev20241.checklistfolha.databinding.FragmentControleQuadrosBinding
import br.pucpr.appdev20241.checklistfolha.model.DataStore
import br.pucpr.appdev20241.checklistfolha.model.Quadro
import java.util.Calendar

class ControleQuadros : Fragment() {
    private var _binding: FragmentControleQuadrosBinding? = null
    //private val binding get() = _binding!!
    private lateinit var binding: FragmentControleQuadrosBinding
    private lateinit var adapter: QuadrosAdapter
    private lateinit var gesture: GestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentControleQuadrosBinding.inflate(layoutInflater)

        loadRecyclerView()
        configureRecycleViewEvent()
        addItemConfig()
        configureGesture()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentControleQuadrosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadRecyclerView()
        addItemConfig()
    }

    private fun addItemConfig() {
        val fab: Button = binding.buttonAdd

        fab.setOnClickListener {
            val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.adapter_input_quadros, null)
            val dialogBuilder = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setTitle("Adicionar Entrega de Quadro de Frequência")
            val alertDialog = dialogBuilder.create()
            alertDialog.show()
            val editText = dialogView.findViewById<EditText>(R.id.editText)
            val datePicker = dialogView.findViewById<DatePicker>(R.id.datePicker)

            dialogView.findViewById<Button>(R.id.btnAddItem).setOnClickListener {
                val text = editText.text.toString()
                val year = datePicker.year
                val month = datePicker.month
                val day = datePicker.dayOfMonth
                val calendar = Calendar.getInstance()

                calendar.set(year,month,day)
                DataStore.addQuadro(Quadro(text, calendar.time))
                adapter.notifyDataSetChanged()
                Toast.makeText(requireContext(), "Quadro de Frequência entregue com sucesso!", Toast.LENGTH_LONG).show()
                alertDialog.dismiss()

            }
        }
    }

    private fun loadRecyclerView() {
        LinearLayoutManager(requireContext()).run {
            this.orientation = LinearLayoutManager.VERTICAL
            binding.rcvQuadros.layoutManager = this
            adapter = QuadrosAdapter(DataStore.quadrosEntregues)
            binding.rcvQuadros.adapter = adapter
        }
    }

    private fun configureGesture() {
        gesture = GestureDetector(requireContext(), object: GestureDetector.SimpleOnGestureListener(){
            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                binding.rcvQuadros.findChildViewUnder(e.x, e.y)?.let { view ->
                    val position = binding.rcvQuadros.getChildAdapterPosition(view)
                    showEditDialog(position)
                }
                return true
            }

            override fun onLongPress(e: MotionEvent) {
                super.onLongPress(e)
                binding.rcvQuadros.findChildViewUnder(e.x, e.y).run {
                    this?.let { view ->
                        binding.rcvQuadros.getChildAdapterPosition(view).apply {
                            val quadroFreq = DataStore.getQuadro(this)
                            AlertDialog.Builder(requireContext()).run {
                                setMessage("Tem certeza que deseja remover este Quadro?")
                                setNegativeButton("Cancelar") {_,_ ->}
                                setPositiveButton("Excluir") { _,_ ->
                                    DataStore.removeQuadro(this@apply)
                                    Toast.makeText(requireContext(), "Quadro ${quadroFreq.quadroLocal} removido com sucesso!", Toast.LENGTH_LONG).show()
                                    adapter.notifyDataSetChanged()
                                }
                            }.show()
                        }
                    }
                }
            }
        })
    }

    private fun showEditDialog(position: Int) {
        val quadroItem = DataStore.getQuadro(position)
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.adapter_edit_quadros, null)
        val editText = dialogView.findViewById<EditText>(R.id.editText).apply {
            setText(quadroItem.quadroLocal)
        }
        val datePicker = dialogView.findViewById<DatePicker>(R.id.datePicker)
        val calendar = Calendar.getInstance().apply { time = quadroItem.dataEntrega }
        datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

        AlertDialog.Builder(requireContext()).run {
            setView(dialogView)
            setTitle("Editar Entrega de Quadro")
            setPositiveButton("Salvar") { _, _ ->
                quadroItem.quadroLocal = editText.text.toString()
                val year = datePicker.year
                val month = datePicker.month
                val day = datePicker.dayOfMonth
                calendar.set(year, month, day)
                quadroItem.dataEntrega = calendar.time
                adapter.notifyItemChanged(position)
                Toast.makeText(requireContext(), "Quadro editado com sucesso!", Toast.LENGTH_LONG).show()
            }
            setNegativeButton("Cancelar", null)
        }.show()
    }

    private fun configureRecycleViewEvent() {
        binding.rcvQuadros.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                rv.findChildViewUnder(e.x, e.y). run {
                    return (this != null && gesture.onTouchEvent(e))
                }
            }
            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}