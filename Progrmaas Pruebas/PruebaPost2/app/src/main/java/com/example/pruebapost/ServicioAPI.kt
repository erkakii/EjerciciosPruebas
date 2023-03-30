package com.example.pruebapost

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ServicioAPI {
    @POST("usuarios")
    fun crearUsuario(@Body usuario: Usuario): Call<Void>
}
