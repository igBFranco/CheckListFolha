package br.pucpr.appdev20241.checklistfolha.view

import android.net.Uri
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
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.pucpr.appdev20241.checklistfolha.R
import br.pucpr.appdev20241.checklistfolha.databinding.FragmentControleQuadrosBinding
import br.pucpr.appdev20241.checklistfolha.model.DataStore
import br.pucpr.appdev20241.checklistfolha.model.Quadro
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date
import java.util.UUID

class ControleQuadros : Fragment() {
    private var _binding: FragmentControleQuadrosBinding? = null
    private lateinit var binding: FragmentControleQuadrosBinding
    private lateinit var adapter: QuadrosAdapter
    private lateinit var gesture: GestureDetector

    private lateinit var imageViewQuadro: ImageView
    private var selectedImageUri: Uri? = null

    private val selectImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            selectedImageUri = it
            imageViewQuadro.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentControleQuadrosBinding.inflate(layoutInflater)
        configureRecycleViewEvent()
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
        viewLifecycleOwner.lifecycleScope.launch {
            loadRecyclerView()
            addItemConfig()
        }
    }

    private fun addItemConfig() {
        val fab: Button = binding.buttonAdd

        fab.setOnClickListener {
            val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.adapter_input_quadros, null)
            imageViewQuadro = dialogView.findViewById(R.id.imageViewQuadro)
            val buttonSelectImage: Button = dialogView.findViewById(R.id.buttonSelectImage)

            buttonSelectImage.setOnClickListener {
                openImageSelector()
            }

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
                calendar.set(year, month, day)

                viewLifecycleOwner.lifecycleScope.launch {
                    if (selectedImageUri != null) {
                        saveQuadroWithImage(text, calendar.time)
                    } else {
                        saveQuadroWithoutImage(text, calendar.time)
                    }
                    val updatedList = DataStore.getAllQuadros().toMutableList()
                    withContext(Dispatchers.Main) {
                        adapter.updateData(updatedList)
                        Toast.makeText(requireContext(), "Quadro de Frequência entregue com sucesso!", Toast.LENGTH_LONG).show()
                        alertDialog.dismiss()
                    }
                }
            }
        }
    }

    private fun openImageSelector() {
        selectImageLauncher.launch("image/*")
    }

    private suspend fun saveQuadroWithImage(local: String, dataEntrega: Date) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val storageReference = FirebaseStorage.getInstance().reference
        val imageRef = storageReference.child("images/$uid/${System.currentTimeMillis()}.jpg")

        try {
            val uploadTask = imageRef.putFile(selectedImageUri!!).await()
            val downloadUrl = imageRef.downloadUrl.await()
            val quadro = Quadro(
                quadroLocal = local,
                dataEntrega = dataEntrega,
                imageUrl = downloadUrl.toString()
            )
            DataStore.addQuadroItem(quadro)
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(requireContext(), "Erro ao salvar o Quadro com imagem", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun saveQuadroWithoutImage(local: String, dataEntrega: Date) {
        val quadro = Quadro(
            quadroLocal = local,
            dataEntrega = dataEntrega
        )
        DataStore.addQuadroItem(quadro)
    }

    private suspend fun loadRecyclerView() {
        val quadros = DataStore.getAllQuadros().toMutableList()
        LinearLayoutManager(requireContext()).run {
            this.orientation = LinearLayoutManager.VERTICAL
            binding.rcvQuadros.layoutManager = this
            adapter = QuadrosAdapter(quadros)
            binding.rcvQuadros.adapter = adapter
        }
    }

    private fun configureGesture() {
        gesture = GestureDetector(requireContext(), object : GestureDetector.SimpleOnGestureListener() {
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
                            viewLifecycleOwner.lifecycleScope.launch {
                                val quadroFreq = DataStore.getQuadroItem(this@apply)
                                AlertDialog.Builder(requireContext()).run {
                                    setMessage("Tem certeza que deseja remover este Quadro?")
                                    setNegativeButton("Cancelar") { _, _ -> }
                                    setPositiveButton("Excluir") { _, _ ->
                                        viewLifecycleOwner.lifecycleScope.launch {
                                            DataStore.deleteQuadroItemByPosition(this@apply)
                                            val updatedList = DataStore.getAllQuadros().toMutableList()
                                            withContext(Dispatchers.Main) {
                                                adapter.updateData(updatedList)
                                                Toast.makeText(requireContext(), "Quadro ${quadroFreq?.quadroLocal} removido com sucesso!", Toast.LENGTH_LONG).show()
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
            val quadroItem = DataStore.getQuadroItem(position)
            val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.adapter_edit_quadros, null)
            val editText = dialogView.findViewById<EditText>(R.id.editText).apply {
                setText(quadroItem?.quadroLocal)
            }
            val datePicker = dialogView.findViewById<DatePicker>(R.id.datePicker)
            val imageView = dialogView.findViewById<ImageView>(R.id.imageViewQuadroEdit)
            val buttonSelectImage = dialogView.findViewById<Button>(R.id.buttonSelectImageEdit)

            quadroItem?.imageUrl?.let { imageUrl ->
                Glide.with(this@ControleQuadros)
                    .load(imageUrl)
                    .into(imageView)
            }

            buttonSelectImage.setOnClickListener {
                selectImageLauncher.launch("image/*")
            }

            val alertDialog = AlertDialog.Builder(requireContext()).run {
                setView(dialogView)
                setTitle("Editar Entrega de Quadro")
                setPositiveButton("Salvar") { _, _ ->
                    val updatedText = editText.text.toString()
                    val year = datePicker.year
                    val month = datePicker.month
                    val day = datePicker.dayOfMonth
                    val calendar = Calendar.getInstance()
                    calendar.set(year, month, day)
                    val updatedDate = calendar.time

                    if (selectedImageUri != null) {
                        viewLifecycleOwner.lifecycleScope.launch {
                            val imageUrl = uploadImageToFirebaseStorage(selectedImageUri!!)
                            quadroItem?.imageUrl = imageUrl
                            updateQuadro(quadroItem, updatedText, updatedDate)
                        }
                    } else {
                        updateQuadro(quadroItem, updatedText, updatedDate)
                    }
                }
                setNegativeButton("Cancelar", null)
            }.show()
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(resources.getColor(R.color.red))
        }
    }

    private fun updateQuadro(quadroItem: Quadro?, updatedText: String, updatedDate: Date) {
        quadroItem?.apply {
            quadroLocal = updatedText
            dataEntrega = updatedDate
            viewLifecycleOwner.lifecycleScope.launch {
                DataStore.editQuadroItem(this@apply)
                val updatedList = DataStore.getAllQuadros().toMutableList()
                adapter.updateData(updatedList)
                Toast.makeText(requireContext(), "Quadro editado com sucesso!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private suspend fun uploadImageToFirebaseStorage(imageUri: Uri): String {
        val storageReference = FirebaseStorage.getInstance().reference
        val imageRef = storageReference.child("quadros/${UUID.randomUUID()}.jpg")
        imageRef.putFile(imageUri).await()
        return imageRef.downloadUrl.await().toString()
    }


    private fun configureRecycleViewEvent() {
        binding.rcvQuadros.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                rv.findChildViewUnder(e.x, e.y).run {
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
