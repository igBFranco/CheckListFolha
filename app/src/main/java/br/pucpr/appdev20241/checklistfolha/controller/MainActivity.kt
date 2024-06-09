package br.pucpr.appdev20241.checklistfolha.controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import androidx.fragment.app.Fragment
import br.pucpr.appdev20241.checklistfolha.R
import br.pucpr.appdev20241.checklistfolha.databinding.ActivityMainBinding
import br.pucpr.appdev20241.checklistfolha.model.DataStore
import br.pucpr.appdev20241.checklistfolha.view.CheckList
import br.pucpr.appdev20241.checklistfolha.view.ControleQuadros
import br.pucpr.appdev20241.checklistfolha.view.Fechamento
import br.pucpr.appdev20241.checklistfolha.view.ItemsAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ItemsAdapter
    private lateinit var gesture: GestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataStore.init(this)

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
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

}