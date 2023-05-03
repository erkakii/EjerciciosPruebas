package com.example.eticklets

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth

class CreateAccountActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        val txtEmail : TextView = findViewById(R.id.txtEmailCreate)
        val txtNombre : TextView = findViewById(R.id.txtNombreCreate)
        val txtPass : TextView = findViewById(R.id.txtpasswordCretate)
        val txtPassConfirm : TextView = findViewById(R.id.txtPasswordConfirm)
        val errorNulos : TextView = findViewById(R.id.txtErrorNulosCreate)
        val imgErrorPass : ImageView = findViewById(R.id.imgErrorPass)
        val imgErrorConfirm : ImageView = findViewById(R.id.imgErrorConfirm)
        val imgErrorNulos : ImageView = findViewById(R.id.imgErrorNulls)
        val btnRegistrarse : Button = findViewById(R.id.btnRegistrarse)

        btnRegistrarse.setOnClickListener {
            val nulosOk : Boolean
            val contraseniasOk: Boolean
            val pass1 = txtPass.text.toString()
            val pass2 = txtPassConfirm.text.toString()


            if (comprobarNull(txtEmail.text.toString(), txtPass.text.toString(),txtNombre.text.toString())){
                imgErrorNulos.isVisible = true
                errorNulos.isVisible = true
                nulosOk = false;
            }else{
                nulosOk = true
                imgErrorNulos.isVisible = false
                errorNulos.isVisible = false
            }
            if (pass1 == pass2){
                contraseniasOk = true
                imgErrorPass.isVisible = false
                imgErrorConfirm.isVisible = false
            }else{
                imgErrorPass.isVisible = true
                imgErrorConfirm.isVisible = true
                contraseniasOk = false;
            }

            if (nulosOk && contraseniasOk){
                createAccount(txtEmail.text.toString(), txtPass.text.toString())
            }
        }


        firebaseAuth = FirebaseAuth.getInstance()
    }

    private fun comprobarNull(email: String, password: String,  nombre : String): Boolean {
        var nulos = false;

        if (email.isEmpty() || password.isEmpty() || nombre.isEmpty()){
            nulos = true;
        }

        return nulos;

    }

    private fun enviarVerificacionEmail() {
        val user = firebaseAuth.currentUser
        user?.sendEmailVerification()
    }

    private fun createAccount(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = firebaseAuth.currentUser
                    Toast.makeText(baseContext, "Cuenta creada correctamente, verifique su correo", Toast.LENGTH_SHORT)
                        .show()
                    enviarVerificacionEmail()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(baseContext, "Error al crear la cuenta", Toast.LENGTH_SHORT)
                        .show()
                    //updateUI(null)
                }
            }
    }
}