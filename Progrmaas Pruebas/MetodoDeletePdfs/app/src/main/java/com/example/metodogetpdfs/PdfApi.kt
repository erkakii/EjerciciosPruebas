package com.example.metodogetpdfs

import retrofit2.http.DELETE
import retrofit2.http.GET

interface PdfApi {
    @GET("pdf")
    suspend fun getPdfs(): MutableList<Pdf>

    @DELETE("pdf/{id}")
    suspend fun deletePdf(id: Int)
}