package com.example.api

import java.time.LocalDateTime

data class UserResponse(var idUsuario: Int, var nombre: String, var email: String, var contrasenia: String, var fechaRegistro: LocalDateTime)

fun UserResponse.toUser(): User {
    return User(
        nombre = this.nombre,
        email = this.email,
        contrasenia = this.contrasenia,
        fechaRegistro = this.fechaRegistro
    )
}