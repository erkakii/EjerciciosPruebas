package com.example.metodogetpdfs

import android.annotation.SuppressLint
import android.app.AlertDialog
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
        val btnSort: Button = findViewById(R.id.btnSort)
        var pdfs: MutableList<Pdf> = mutableListOf()
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
                        val listaux: List<Pdf> = pdfs
                        pdfs = listaux.sortedBy { it.titulo }.toMutableList()
                        isSortedByDate = false
                        btnSort.text = "Ordenar por fecha"
                    } else {
                        val listaaux: List<Pdf> = pdfs
                        pdfs = listaaux.sortedByDescending { it.fechaSubida }.toMutableList()
                        isSortedByDate = true
                        btnSort.text = "Ordenar por comercio"
                    }
                    pdfAdapter = PdfAdapter(pdfs, { pdfItem ->
                        val intent = Intent(applicationContext, PdfViewActivity::class.java)
                        intent.putExtra("pdfbase64", pdfItem.contenidoPDF)
                        startActivity(intent)
                    }, { pdfItem ->
                        val builder = AlertDialog.Builder(this@MainActivity)
                        builder.setMessage("¿Está seguro que desea eliminar el PDF?")
                            .setCancelable(false)
                            .setPositiveButton("Sí") { _, _ ->
                                pdfs.remove(pdfItem)
                                pdfAdapter.notifyDataSetChanged()
                                lifecycleScope.launch {
                                    try {
                                        pdfApi.deletePdf(pdfItem.idPdf)
                                        Toast.makeText(this@MainActivity, "PDF eliminado", Toast.LENGTH_LONG).show()
                                    } catch (e: Exception) {
                                        Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                            .setNegativeButton("No") { dialog, _ ->
                                dialog.dismiss()
                            }
                        val alert = builder.create()
                        alert.show()
                    })
                    pdfAdapter.notifyDataSetChanged()
                    recyclerView.adapter = pdfAdapter
                    recyclerView.layoutManager = LinearLayoutManager(applicationContext)
                }

                val listaaux: List<Pdf> = pdfs
                pdfs = listaaux.sortedByDescending { it.fechaSubida }.toMutableList()
                pdfAdapter = PdfAdapter(pdfs, { pdfItem ->
                    val intent = Intent(applicationContext, PdfViewActivity::class.java)
                    intent.putExtra("pdfbase64", pdfItem.contenidoPDF)
                    startActivity(intent)
                },{ pdfItem ->
                    val builder = AlertDialog.Builder(this@MainActivity)
                    builder.setMessage("¿Está seguro que desea eliminar el PDF?")
                        .setCancelable(false)
                        .setPositiveButton("Sí") { _, _ ->
                            pdfs.remove(pdfItem)
                            pdfAdapter.notifyDataSetChanged()
                            lifecycleScope.launch {
                                try {
                                    pdfApi.deletePdf(pdfItem.idPdf)
                                    Toast.makeText(this@MainActivity, "PDF eliminado", Toast.LENGTH_LONG).show()
                                } catch (e: Exception) {
                                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
                                }
                            }
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
                Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
            }
        }

    }
}