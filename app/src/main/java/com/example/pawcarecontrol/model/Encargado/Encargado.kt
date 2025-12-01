package com.example.pawcarecontrol.model.Encargado

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET

// Modelo actualizado para reflejar la respuesta completa de la API
data class Encargado(
    @SerializedName("id") val id: Int,
    @SerializedName("nombres") val nombres: String,
    @SerializedName("apellidos") val apellidos: String,
    @SerializedName("dui") val dui: String,
    @SerializedName("edad") val edad: String,
    @SerializedName("ciudad") val ciudad: String,
    @SerializedName("direccion") val direccion: String,
    @SerializedName("estatus") val estatus: Int
)

interface EncargadoService {
    @GET("Encargado_Mascota/All") // Este endpoint obtiene la lista de todos los encargados
    suspend fun getEncargados(): List<Encargado>
}

object EncargadoClient : com.example.pawcarecontrol.model.BaseClient() {
    val service: EncargadoService by lazy {
        retrofit.create(EncargadoService::class.java)
    }
}
