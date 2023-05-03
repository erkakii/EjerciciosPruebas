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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.security.MessageDigest

class LoginActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        val btnLogin: Button = findViewById(R.id.btnLogin)
        val txtemail: TextView = findViewById(R.id.etyEmailLogin)
        val txtpass: TextView = findViewById(R.id.etyPasswordLogin)
        val txtCrearCuenta: TextView = findViewById(R.id.txtRegistrarse)
        val txtRecPass: TextView = findViewById(R.id.txtOlvidarPass)
        val imgError1: ImageView = findViewById(R.id.imgErrorLogin1)
        val imgError2: ImageView = findViewById(R.id.imgErrorLogin2)
        val txtErrorEmail: TextView = findViewById(R.id.txtErrorLoginEmail)
        val txtErrorPass: TextView = findViewById(R.id.txtErrorLoginEmail2)
        val txtPeligro : TextView = findViewById(R.id.txtPeligro)
        val imgPeligro : ImageView = findViewById(R.id.imgPeligro)


        firebaseAuth = Firebase.auth

        btnLogin.setOnClickListener {
            if (!comprobarNull(txtemail.text.toString(), txtpass.text.toString(), imgError1, imgError2, txtErrorEmail, txtErrorPass)) {
                login(txtemail.text.toString(), txtpass.text.toString(), txtPeligro, imgPeligro)
            }
        }

        txtRecPass.setOnClickListener {
            val intent = Intent(this, RecuperatePasswordActivity::class.java)
            startActivity(intent)
        }

        txtCrearCuenta.setOnClickListener {
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        return
    }


    private fun comprobarNull(
        email: String,
        password: String,
        imgError1: ImageView,
        imgError2: ImageView,
        txtErrorEmail: TextView,
        txtErrorPass: TextView
    ): Boolean {
        var nulo = false

        if (email.isEmpty()) {
            imgError1.isVisible = true
            txtErrorEmail.isVisible = true
            nulo = true;
        } else {
            imgError1.isVisible = false
            txtErrorEmail.isVisible = false
        }

        if (password.isEmpty()) {
            imgError2.isVisible = true
            txtErrorPass.isVisible = true
            nulo = true
        } else {
            imgError2.isVisible = false
            txtErrorPass.isVisible = false
        }

        return nulo;
    }



    private fun login(email: String, password: String, txtPeligro : TextView, imgPeligro : ImageView) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = firebaseAuth.currentUser
                    val verifica = user?.isEmailVerified
                    if (verifica == true) {
                        Toast.makeText(baseContext, "Login correcto", Toast.LENGTH_SHORT).show()
                        txtPeligro.isVisible = false
                        imgPeligro.isVisible = false
                        val intent = Intent(this, InicioActivity::class.java)
//                        intent.putExtra("email", email)
//                        val md = MessageDigest.getInstance("SHA-256")
//                        val bytes = md.digest(password.toByteArray())
//                        val hash = bytes.joinToString("") { "%02x".format(it) }
//                        intent.putExtra("password", hash)
                        startActivity(intent)
                        finish()
                    } else {
                        txtPeligro.isVisible = true
                        imgPeligro.isVisible = true
                    }
                } else {
                    Toast.makeText(baseContext, "Login incorrecto", Toast.LENGTH_SHORT).show()
                }
            }


    }


}