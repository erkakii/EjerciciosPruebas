package com.example.pruebapostpdf

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
//interfaz con la que consumo la api
interface ServicioAPI {
    @POST("pdf")
    fun crearPdf(@Body pdf: Pdfs): Call<Void>
}