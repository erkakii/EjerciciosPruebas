package com.example.ejerciciopost

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface UserService {
    @POST("users")
    fun createUser(@Body user: User): Call<User>


    @GET
    suspend fun getUsers(@Url url: String): Response<List<User>>
}