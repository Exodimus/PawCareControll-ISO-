package com.example.pawcarecontrol.model.User

import retrofit2.http.GET
import retrofit2.http.Path

interface UserService {
    @GET("Usuario/FindByCorreo/{correo}/{contra}")
    suspend fun getUserByEmailAndPass(@Path("correo")correo:String, @Path("contra")contra:String): User
}