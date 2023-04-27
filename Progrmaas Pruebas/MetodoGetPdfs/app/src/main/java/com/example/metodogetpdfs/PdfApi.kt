package com.example.metodogetpdfs

import retrofit2.http.GET

interface PdfApi {
    @GET("pdf")
    suspend fun getPdfs(): List<Pdf>
}