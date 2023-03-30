package com.example.ejerciciopost

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class User(
    @SerializedName("idUsuario") val id: Int,
    @SerializedName("nombre") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("contrasenia") val password: String,
    @SerializedName("fechaRegistro") val registrationDate: LocalDateTime
)
