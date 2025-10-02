package com.example.pawcarecontrol.model.Doctor

import com.example.pawcarecontrol.model.User.UserType

data class Doctor (
    val id: Int,
    val apellidos: String,
    val correo: String,
    val nombres: String,
    val genero: String,
    val pass: String,
)

data class PostDoctor (
    val nombres: String,
    val apellidos: String,
    val pass: String,
    val correo: String,
    val estatus: Long,
    val tipoUsuario: UserType,
//    val genero: String,
)