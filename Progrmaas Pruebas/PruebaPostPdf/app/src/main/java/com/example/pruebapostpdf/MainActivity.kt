package com.example.pruebapostpdf

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
import java.net.URL
import java.util.*

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://eticketsapi.azurewebsites.net/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()



        val url = "https://campuscreativo.cl/wp-content/uploads/2020/03/random-on-line.pdf"
        DownloadPdfTask(object : DownloadPdfTask.DownloadPdfListener {
            override fun onDownloadComplete(pdfBytes: ByteArray) {
                // Aquí puedes guardar el PDF en base64 en tu base de datos
                val pdfBase64 = Base64.getEncoder().encodeToString(pdfBytes)
                Log.d("MainActivity", pdfBase64)
                val servicioAPI = retrofit.create(ServicioAPI::class.java)
                val usuario = Pdfs(
                    titulo = "Mercadona",
                    contenido = pdfBase64,
                    emailUser = "elminikaki@gmail.com"
                )
                servicioAPI.crearUsuario(usuario).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        Log.d("MainActivity", "onResponse: ${response.body()}")
                        if (response.isSuccessful) {
                            // La llamada fue exitosa, se ha creado el usuario
                            Toast.makeText(this@MainActivity, "pdf descargado", Toast.LENGTH_SHORT).show()
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

            override fun onDownloadFailed(errorMessage: String) {
                // Maneja el error aquí
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }).execute(url)

    }


}