package com.example.pruebapostpdf
//Data class con la que contruyo el objeto pdf para luego enviarlo a la base de datos
data class Pdfs(
    val titulo: String,
    val contenidoPDF: String,
    val emailUser: String)
