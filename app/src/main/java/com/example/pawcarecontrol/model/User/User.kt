package com.example.pawcarecontrol.model.User

data class User(
    val apellidos: String,
    val correo: String,
    val estatus: Int,
    val id: Int,
    val nombres: String,
    val pass: String,
    val tipoUsuario: UserType
)