package com.example.eticklets

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth

class RecuperatePasswordActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperate_password)
        val email: TextView = findViewById(R.id.etyEmailRecuperate)
        val imgError: ImageView = findViewById(R.id.imgErrorOlvidate)
        val txtError: TextView = findViewById(R.id.txtErrorLoginEmail3)
        val btnEnviar: Button = findViewById(R.id.btnEnviarOlvido)

        btnEnviar.setOnClickListener {
            if (!comprobarNull(email.text.toString(), imgError, txtError)) {
                sendPasswordResetEmail(email.text.toString())
            }
        }


        firebaseAuth = FirebaseAuth.getInstance()
    }

    private fun sendPasswordResetEmail(email: String) {
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        baseContext,
                        "Se ha enviado un correo para recuperar la contraseña",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(
                        baseContext,
                        "No se ha podido enviar el correo",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

    }

    private fun comprobarNull(email: String, imgError: ImageView, txtError: TextView): Boolean {
        var correcto = true;

        if (email.isEmpty()) {
            imgError.isVisible = true
            txtError.isVisible = true
        } else {
            imgError.isVisible = false
            txtError.isVisible = false
            correcto = false
        }
        return correcto
    }

}
