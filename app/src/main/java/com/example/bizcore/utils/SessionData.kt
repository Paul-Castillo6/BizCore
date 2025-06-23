package com.example.bizcore.utils

object SessionData {

    var codigoTemporal: String? = null
    var nombresUsuario: String? = null
        var idUsuario: Int = 0
        var dniUsuario: String = ""
        var correoUsuario: String = ""
        var rolUsuario: Int = 0
        var rolNombre: String = ""
        var estadoUsuario: Int = 0
        var estadoNombre: String = ""
        var fotoBase64: String? = null

    fun clear() {
        codigoTemporal = null
        nombresUsuario = null
        idUsuario = 0
        dniUsuario = ""
        correoUsuario = ""
        rolUsuario = 0
        rolNombre = ""
        estadoUsuario = 0
        estadoNombre = ""
        fotoBase64 = null
    }

}
