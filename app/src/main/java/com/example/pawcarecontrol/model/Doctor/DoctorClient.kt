package com.example.pawcarecontrol.model.Doctor

import com.example.pawcarecontrol.model.BaseClient

object DoctorClient: BaseClient() {
   val service = retrofit.create(DoctorService::class.java)
}