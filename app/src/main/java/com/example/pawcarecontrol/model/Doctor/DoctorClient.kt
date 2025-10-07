package com.example.pawcarecontrol.model.Doctor

import android.content.Context
import com.example.pawcarecontrol.model.BaseClient



class DoctorClient(context: Context) : BaseClient(context) {
   val service = retrofit.create(DoctorService::class.java)
}
