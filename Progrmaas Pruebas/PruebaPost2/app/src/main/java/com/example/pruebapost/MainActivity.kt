package com.example.pruebapost

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://eticketsapi.azurewebsites.net/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val servicioAPI = retrofit.create(ServicioAPI::class.java)
        val usuario = Usuario(
            nombre = "Juan",
            email = "juan@gmail.com",
            contrasenia = "contraseña123"
        )


        servicioAPI.crearUsuario(usuario).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Log.d("MainActivity", "onResponse: ${response.body()}")
                if (response.isSuccessful) {
                    // La llamada fue exitosa, se ha creado el usuario
                    Toast.makeText(this@MainActivity, "Usuario creado", Toast.LENGTH_SHORT).show()
                } else {
                    // La llamada falló por algún motivo
                    Toast.makeText(this@MainActivity, response.toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // La llamada falló debido a un error de red u otro tipo de error
                Toast.makeText(this@MainActivity, t.toString(), Toast.LENGTH_SHORT).show()
            }
        })

    }
}