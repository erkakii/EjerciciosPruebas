package com.example.eticklets

import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface PdfApi {
    @GET("pdf/{email}")
    suspend fun getPdfs(@Path("email") email: String): MutableList<PdfsGet>

    @DELETE("pdf/{id}")
    suspend fun deletePdf(@Path("id") id: Int): Response<Void>
}