package com.example.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url
import java.time.LocalDateTime

interface APIService {

    @GET
    fun getUsers(@Url url : String): Response<List<UserResponse>>

    @POST
    fun postUser(@Body usuario : User)

}
