package com.example.metodogetpdfs

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.barteksc.pdfviewer.PDFView

class PdfViewActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_view)

        val base64String = intent.getStringExtra("pdfbase64")
        val pdfView = findViewById<PDFView>(R.id.pdfView)

        // Decodifica el string Base64 a un ByteArray
        val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)

        // Carga el ByteArray en el PDFView
        pdfView.fromBytes(decodedBytes)
            .defaultPage(0)
            .onError { error ->
                Toast.makeText(this, "Error al cargar el PDF: $error", Toast.LENGTH_SHORT).show()
            }
            .load()

    }
}
