package br.pucpr.appdev20241.checklistfolha.controller

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import br.pucpr.appdev20241.checklistfolha.R
import br.pucpr.appdev20241.checklistfolha.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity: AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            val uid = user?.uid
                            Log.d("Auth", "user ID: $uid")
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(baseContext, "Falha na autenticação.", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Por favor, insira o email e a senha.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.resetPasswordButton.setOnClickListener {
            showResetPasswordDialog()
        }
        binding.createAccountButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showResetPasswordDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.adapter_reset_password_form, null)
        val emailEditText = dialogView.findViewById<EditText>(R.id.email)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Redefinir Senha")
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Enviar Email"){ _, _ ->
                val email = emailEditText.text.toString()
                if (email.isNotEmpty()) {
                    auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Email de redefinição de senha enviado.", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this, "Erro ao enviar email de redefinição.", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Por favor, insira um email.", Toast.LENGTH_SHORT).show()
                }
            }
            .create()

        dialog.show()

    }
}