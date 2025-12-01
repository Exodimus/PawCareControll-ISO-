package com.example.pawcarecontrol.model.Appointment

import com.example.pawcarecontrol.model.BaseClient

// AppointmentClient ahora hereda de BaseClient
object AppointmentClient : BaseClient() {
    // Crea una instancia de AppointmentService utilizando Retrofit y lazy para inicializarla solo cuando sea necesario
    val service: AppointmentService by lazy {
        retrofit.create(AppointmentService::class.java)
    }
}
