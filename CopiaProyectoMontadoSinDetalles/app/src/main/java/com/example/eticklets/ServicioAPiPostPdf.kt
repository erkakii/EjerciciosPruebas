package com.example.eticklets

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ServicioAPiPostPdf {
    @POST("pdf")
    fun crearPdf(@Body pdf: PdfsPost): Call<Void>
}