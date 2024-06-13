package br.pucpr.appdev20241.checklistfolha.controller

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import br.pucpr.appdev20241.checklistfolha.R

class SplashActivity : AppCompatActivity() {

    private val SPLASH_DISPLAY_LENGTH = 1000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
            finish()
        }, SPLASH_DISPLAY_LENGTH.toLong())
    }
}