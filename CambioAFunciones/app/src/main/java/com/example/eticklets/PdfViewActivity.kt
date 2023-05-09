package com.example.eticklets

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Base64
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import com.github.barteksc.pdfviewer.PDFView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.ByteArrayOutputStream
import java.io.File

class PdfViewActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_view)

        val base64String = intent.getStringExtra("pdfbase64")
        val pdfView = findViewById<PDFView>(R.id.pdfView)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val fabShare = findViewById<FloatingActionButton>(R.id.fab_share)


        // Decodifica el string Base64 a un ByteArray
        val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)

        // Carga el ByteArray en el PDFView
        pdfView.fromBytes(decodedBytes)
            .defaultPage(0)
            .enableSwipe(true)
            .swipeHorizontal(false)
            .onPageError { page, t ->
                Toast.makeText(this, "Error al cargar la página $page", Toast.LENGTH_LONG).show()
            }
            .load()

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        fabShare.setOnClickListener {
            val pdfFile = File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "e-Ticket.pdf")
            pdfFile.writeBytes(decodedBytes)

            val pdfUri = FileProvider.getUriForFile(this, "com.example.eticklets.provider", pdfFile)

            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "application/pdf"
            shareIntent.putExtra(Intent.EXTRA_STREAM, pdfUri)
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            startActivity(Intent.createChooser(shareIntent, "Compartir PDF"))
        }

    }

}