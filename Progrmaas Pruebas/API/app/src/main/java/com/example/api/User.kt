package com.example.api

import java.time.LocalDateTime

class User(
    val idUsuario: Int,
    val nombre: String,
    val email: String,
    val contrasenia: String,
    val fechaRegistro: LocalDateTime
) {
    constructor(nombre: String, email: String, contrasenia: String, fechaRegistro: LocalDateTime) : this(
        0, // idUsuario será generado automáticamente por la base de datos
        nombre,
        email,
        contrasenia,
        fechaRegistro
    )
}
