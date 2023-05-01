package com.example.metodogetpdfs

import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface PdfApi {
    @GET("pdf")
    suspend fun getPdfs(): MutableList<Pdf>

    @DELETE("pdf/{id}")
    suspend fun deletePdf(@Path("id") id: Int) : Response<Void>
}