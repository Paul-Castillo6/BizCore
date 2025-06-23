package com.example.bizcore.service

import com.example.bizcore.models.LoginRequest
import com.example.bizcore.models.LoginResponse
import com.example.bizcore.models.Producto
import com.example.bizcore.models.ProductoResumen
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("api/apilogin/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @GET("api/apiproducto/resumen")
    fun obtenerResumen(): Call<ProductoResumen>

    @GET("api/apiproducto/ultimas-actualizaciones")
    fun obtenerUltimasActualizaciones(): Call<List<Producto>>

}
