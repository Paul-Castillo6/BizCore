package com.example.bizcore.models

data class LoginResponse(
    val mensaje: String,
    val codigoTemporal: Int,
    val idUsuario: Int,
    val dni: String,
    val nombres: String,
    val rol: Int,
    val rolNombre: String,
    val estado: Int,
    val estadoNombre: String,
    val correo: String,
    val foto: String?
)