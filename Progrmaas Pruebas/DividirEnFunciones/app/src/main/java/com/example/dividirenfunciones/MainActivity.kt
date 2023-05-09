package com.example.dividirenfunciones

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.isVisible
import com.example.dividirenfunciones.ui.theme.DividirEnFuncionesTheme
import java.util.Base64

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}

fun postPdfs(contenido : ByteArray){
    val pdfBase64 = Base64.getEncoder().encodeToString(contenido)
    val retrofit = Retrofit.Builder()
        .baseUrl("https://eticketsapi.azurewebsites.net/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    //Instanciamos la llamada a la interfaz
    val servicioAPI = retrofit.create(ServicioAPiPostPdf::class.java)
    val email : String = intent.getStringExtra("email").toString()
    //Creamos el objeto pdf con el que vamos a enviar los datos a la base de datos
    val pdf = PdfsPost(
        titulo = respuestaQR[1],
        contenidoPDF = pdfBase64,
        emailUser = email,
    )
    // Decodifica la cadena Base64 a un ByteArray
    val decodedBytes =
        android.util.Base64.decode(pdfBase64, android.util.Base64.DEFAULT)
    servicioAPI.crearPdf(pdf).enqueue(object : Callback<Void> {
        override fun onResponse(call: Call<Void>, response: Response<Void>) {
            Log.d("MainActivity", "onResponse: ${response.body()}")
            if (response.isSuccessful) {
                // La llamada fue exitosa, se ha introducido el pdf
                Toast.makeText(
                    this@InicioActivity,
                    "Pdf insertado",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                // La llamada falló por algún motivo
                Toast.makeText(
                    this@InicioActivity,
                    response.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        override fun onFailure(call: Call<Void>, t: Throwable) {
            // La llamada falló debido a un error de red u otro tipo de error
            Toast.makeText(
                this@InicioActivity,
                t.toString(),
                Toast.LENGTH_SHORT
            ).show()
        }
    })
}


fun getPdfs(){
    val pdfApi = retrofit.create(PdfApi::class.java)
    lifecycleScope.launch {
        try {
            pdfs = pdfApi.getPdfs(email)
            progressBar.isVisible = false
            recyclerView = findViewById(R.id.pdfRecyclerView)
            btnSort.isVisible = true
            btnSortFecha.isVisible = true
            val listaaux: List<PdfsGet> = pdfs
            pdfs = listaaux.sortedByDescending { it.fechaSubida }.toMutableList()
            pdfAdapter = PdfAdapter(pdfs, { pdfItem ->
                val intent = Intent(applicationContext, PdfViewActivity::class.java)
                intent.putExtra("pdfbase64", pdfItem.contenidoPDF)
                startActivity(intent)
            }, { pdfItem ->
                val builder = android.app.AlertDialog.Builder(this@InicioActivity)
                builder.setMessage("¿Está seguro que desea eliminar el PDF?")
                    .setCancelable(false)
                    .setPositiveButton("Sí") { _, _ ->
                        deletePdf()
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                val alert = builder.create()
                alert.show()
            })
            recyclerView.adapter = pdfAdapter
            recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        } catch (e: Exception) {
            progressBar.isVisible = false
        }
    }
}

fun deletePdf(){
    val pdfApi = retrofit.create(PdfApi::class.java)
    lifecycleScope.launch {
        try {
            pdfApi.deletePdf(pdfItem.idPdf)
            pdfs.remove(pdfItem)
            pdfAdapter.notifyDataSetChanged()
            mediaplayer = MediaPlayer.create(this@InicioActivity, R.raw.borrar)
            mediaplayer?.start()
            Toast.makeText(
                this@InicioActivity,
                "PDF eliminado🗞️🗑️",
                Toast.LENGTH_LONG
            ).show()
        } catch (e: Exception) {
            Toast.makeText(
                this@InicioActivity,
                e.message,
                Toast.LENGTH_LONG
            ).show()
        }
    }
}

fun GetPdfAdapter(pdfs: List<PdfsGet>) : PdfAdapter{
    return PdfAdapter(pdfs, { pdfItem ->
        val intent = Intent(applicationContext, PdfViewActivity::class.java)
        intent.putExtra("pdfbase64", pdfItem.contenidoPDF)
        startActivity(intent)
    }, { pdfItem ->
        val builder = android.app.AlertDialog.Builder(this@InicioActivity)
        builder.setMessage("¿Está seguro que desea eliminar el PDF?")
            .setCancelable(false)
            .setPositiveButton("Sí") { _, _ ->
                deletePdf()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    })
}


