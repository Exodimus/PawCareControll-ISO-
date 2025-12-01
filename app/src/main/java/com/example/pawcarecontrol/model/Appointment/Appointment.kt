package com.example.pawcarecontrol.model.Appointment

import com.google.gson.annotations.SerializedName

// Estructura principal de una Cita
data class Appointment(
    @SerializedName("id") val id: Int,
    @SerializedName("fecha") val fecha: String,
    @SerializedName("hora") val hora: String,
    @SerializedName("usuario") val usuario: Usuario,
    @SerializedName("paciente") val paciente: Paciente,
    @SerializedName("estadoCita") val estadoCita: EstadoCita
)

// Clases anidadas que forman parte de la respuesta JSON
data class Usuario(
    @SerializedName("nombres") val nombres: String,
    @SerializedName("apellidos") val apellidos: String
)

data class Paciente(
    @SerializedName("nombre") val nombre: String,
    @SerializedName("raza") val raza: String
)

data class EstadoCita(
    @SerializedName("nombre_Estado_Cita") val nombreEstado: String
)

//Para enviar una nueva cita
data class CreateAppointmentRequest(
    @SerializedName("fecha") val fecha: String,
    @SerializedName("hora") val hora: String,
    @SerializedName("estatus") val estatus: Int = 1, // Valor predeterminado
    @SerializedName("usuario") val usuario: IdWrapper,
    @SerializedName("paciente") val paciente: IdWrapper,
    @SerializedName("estadoCita") val estadoCita: IdWrapper
)

// Clase auxiliar para anidar los IDs
data class IdWrapper(
    @SerializedName("id") val id: Int
)


