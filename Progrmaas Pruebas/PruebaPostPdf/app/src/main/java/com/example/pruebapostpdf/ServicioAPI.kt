package com.example.pruebapostpdf

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ServicioAPI {
    @POST("pdf")
    fun crearUsuario(@Body pdf: Pdfs): Call<Void>
}