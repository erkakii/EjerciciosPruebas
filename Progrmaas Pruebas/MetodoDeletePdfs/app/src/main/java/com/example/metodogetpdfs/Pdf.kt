package com.example.metodogetpdfs

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


data class Pdf(
    val idPdf: Int,
    val titulo: String,
    val contenidoPDF: String,
    val fechaSubida: String,
    val emailUser: String
) {
    val fechaSubidaLocal: LocalDateTime
        @RequiresApi(Build.VERSION_CODES.O)
        get() = LocalDateTime.parse(fechaSubida, DateTimeFormatter.ISO_DATE_TIME)
}


