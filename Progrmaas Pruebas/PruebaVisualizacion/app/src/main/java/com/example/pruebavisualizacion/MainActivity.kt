package com.example.pruebavisualizacion

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.example.pruebapostpdf.DownloadPdfTask
import com.example.pruebapostpdf.Pdfs
import com.example.pruebapostpdf.ServicioAPI
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayInputStream
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var pdfView: PDFView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Creamos retrofit que es lo que mediante la interfaz va ha realizar el metodo post
        val retrofit = Retrofit.Builder()
            .baseUrl("https://eticketsapi.azurewebsites.net/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //Url de donde se encuntra el pdf que vamos a descargar es uno random que encontre por internet
        val url = "https://campuscreativo.cl/wp-content/uploads/2020/03/random-on-line.pdf"
        DownloadPdfTask(object : DownloadPdfTask.DownloadPdfListener {
            override fun onDownloadComplete(pdfBytes: ByteArray) {
                // El pdf se descarga y el array de bytes lo convertmos en un string de base 64
                val pdfBase64 = java.util.Base64.getEncoder().encodeToString(pdfBytes)
                Log.d("MainActivity", pdfBase64)
                //Instanciamos la llamada a la interfaz
                val servicioAPI = retrofit.create(ServicioAPI::class.java)
                //Creamos el objeto pdf con el que vamos a enviar los datos a la base de datos
                val pdf = Pdfs(
                    titulo = "Mercadona",
                    contenido = pdfBase64,
                    emailUser = "elminikaki@gmail.com",
                )

                //ESTA ES LA OPCION DE INTENTAR ABRIRLO CON UNA APLICACION DE LECTURA DE PDF
                pdfView = findViewById<PDFView>(R.id.pdfView)


                // Decodifica la cadena Base64 a un ByteArray
                val decodedBytes = Base64.decode(pdfBase64, Base64.DEFAULT)

                // Carga el ByteArray en PDFView
                pdfView.fromBytes(decodedBytes)
                    .defaultPage(0)
                    .enableSwipe(true)
                    .scrollHandle(DefaultScrollHandle(applicationContext))
                    .load()

//                val base64EncodedPdf = pdfBase64 // cadena base64 que representa el PDF
//                val pdfBytes = Base64.decode(base64EncodedPdf, Base64.DEFAULT)
//
//                val document = PDDocument.load(pdfBytes)
//                val contentStripper = PDFTextStripper()
//                val text = contentStripper.getText(document)

//                val totalRegex = Regex("(?i)Total|Importe:\\s*([\\d,.]+)")
//                val matchResult = totalRegex.find(text)
//                val firstLine = text.lines().first()
//                Log.d("MainActivity", firstLine)

//                val totalValue = matchResult?.groupValues?.get(1) ?: "0"
//                val total = totalValue.replace(",", ".").toDouble()

                //Este metodo es para realizar la llamada al metodo post
                servicioAPI.crearPdf(pdf).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        Log.d("MainActivity", "onResponse: ${response.body()}")
                        if (response.isSuccessful) {
                            // La llamada fue exitosa, se ha creado el usuario
                            Toast.makeText(this@MainActivity, "pdf descargado", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            // La llamada falló por algún motivo
                            Toast.makeText(
                                this@MainActivity,
                                response.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
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
