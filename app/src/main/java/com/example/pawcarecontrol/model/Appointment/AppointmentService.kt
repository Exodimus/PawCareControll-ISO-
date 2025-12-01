package com.example.pawcarecontrol.model.Appointment

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

private const val route = "Cita"

interface AppointmentService {
    @GET("${route}/All")
    suspend fun getAppointments(): List<Appointment>

    //Crear Cita
    @POST("Cita/Save")
    suspend fun createAppointment(@Body appointmentRequest: CreateAppointmentRequest): Response<Void>
}

