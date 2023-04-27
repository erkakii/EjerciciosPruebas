package com.example.metodogetpdfs

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var pdfAdapter: PdfAdapter
    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnSort : Button = findViewById(R.id.btnSort)
        var pdfs : List<Pdf> = emptyList()
        var isSortedByDate = true

        val retrofit = Retrofit.Builder()
            .baseUrl("https://eticketsapi.azurewebsites.net/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val pdfApi = retrofit.create(PdfApi::class.java)

        lifecycleScope.launch {
            try {
                pdfs = pdfApi.getPdfs()
                Toast.makeText(this@MainActivity, "Completado el get", Toast.LENGTH_LONG).show()
                recyclerView = findViewById(R.id.pdfRecyclerView)
                btnSort.isVisible = true
                btnSort.setOnClickListener {
                    if (isSortedByDate) {
                        pdfs = pdfs.sortedBy { it.titulo }
                        isSortedByDate = false
                        btnSort.text = "Ordenar por fecha"
                    } else {
                        pdfs = pdfs.sortedByDescending { it.fechaSubida }
                        isSortedByDate = true
                        btnSort.text = "Ordenar por comerecio"
                    }
                    pdfAdapter = PdfAdapter(pdfs) { pdfItem ->
                        val intent = Intent(applicationContext, PdfViewActivity::class.java)
                        intent.putExtra("pdfbase64", pdfItem.contenidoPDF)
                        startActivity(intent)
                    }
                    pdfAdapter.notifyDataSetChanged()
                    recyclerView.adapter = pdfAdapter
                    recyclerView.layoutManager = LinearLayoutManager(applicationContext)
                }
                pdfAdapter = PdfAdapter(pdfs.sortedByDescending { it.fechaSubida }) { pdfItem ->
                    val intent = Intent(applicationContext, PdfViewActivity::class.java)
                    intent.putExtra("pdfbase64", pdfItem.contenidoPDF)
                    startActivity(intent)
                }
                recyclerView.adapter = pdfAdapter
                recyclerView.layoutManager = LinearLayoutManager(applicationContext)
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
            }
        }




    }
}