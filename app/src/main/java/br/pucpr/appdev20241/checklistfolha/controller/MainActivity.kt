package br.pucpr.appdev20241.checklistfolha.controller

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseApp
import br.pucpr.appdev20241.checklistfolha.R
import br.pucpr.appdev20241.checklistfolha.databinding.ActivityMainBinding
import br.pucpr.appdev20241.checklistfolha.model.DataStore
import br.pucpr.appdev20241.checklistfolha.view.CheckList
import br.pucpr.appdev20241.checklistfolha.view.ControleQuadros
import br.pucpr.appdev20241.checklistfolha.view.Fechamento
import br.pucpr.appdev20241.checklistfolha.view.FechamentosArquivados
import br.pucpr.appdev20241.checklistfolha.view.ItemsAdapter
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ItemsAdapter
    private lateinit var gesture: GestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
            return
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTheme(R.style.Base_Theme_CitiesApp)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CheckList())
                .commit()
        }

        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadFragment(CheckList())
                    true
                }
                R.id.nav_entrega -> {
                    loadFragment(ControleQuadros())
                    true
                }
                R.id.nav_fechamento -> {
                    loadFragment(Fechamento())
                    true
                }
                R.id.nav_arquivo -> {
                    loadFragment(FechamentosArquivados())
                    true
                }
                else -> false
            }
        }

        val logoutButton = findViewById<Button>(R.id.btn_logout)
        logoutButton.setOnClickListener {
            fun logoutUser(context: Context) {
                val auth = FirebaseAuth.getInstance()
                auth.signOut()
                Toast.makeText(context, "Usuário deslogado.", Toast.LENGTH_LONG).show()
            }
            logoutUser(this)
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

}