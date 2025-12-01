package com.example.pawcarecontrol.model.Paciente

import com.example.pawcarecontrol.model.Appointment.IdWrapper
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

// Modelo para los datos que recibimos de la API
data class Paciente(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("raza") val raza: String,
    @SerializedName("edad") val edad: String,
    @SerializedName("encargadoMascota") val encargadoMascota: EncargadoMascota
)

data class EncargadoMascota(
    @SerializedName("nombres") val nombres: String,
    @SerializedName("apellidos") val apellidos: String
)

// Modelo para ENVIAR una nueva mascota (POST)
data class CreatePetRequest(
    @SerializedName("nombre") val nombre: String,
    @SerializedName("raza") val raza: String,
    @SerializedName("fecha_nacimiento") val fecha_nacimiento: String,
    @SerializedName("edad") val edad: String,
    @SerializedName("encargadoMascota") val encargadoMascota: IdWrapper,
    @SerializedName("estatus") val estatus: Long = 1
)

// Interfaz de servicio para Retrofit
interface PacienteService {
    @GET("Paciente_Mascota/All")
    suspend fun getPacientes(): List<Paciente>

    @POST("Paciente_Mascota/Save")
    suspend fun createPet(@Body petRequest: CreatePetRequest): Response<Void>
}

object PacienteClient : com.example.pawcarecontrol.model.BaseClient() {
    val service: PacienteService by lazy {
        retrofit.create(PacienteService::class.java)
    }
}
