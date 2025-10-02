package com.example.pawcarecontrol.model.Doctor

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

private const val route = "Usuario"

interface DoctorService {
    @GET("${route}/All/2")
    suspend fun getDoctors(): MutableList<Doctor>

    @POST("${route}/Save")
    suspend fun createDoctor(@Body user: PostDoctor): Call<PostDoctor>

    @DELETE("${route}/Delete/{id}")
    suspend fun deleteDoctor(@Path("id") id: Int)

    @GET("${route}/Find/{id}")
    suspend fun getDoctor(@Path("id") id: Int): Doctor

    @PUT("${route}/Update/{id}")
    suspend fun updateDoctor(@Path("id") id: Int, @Body user: PostDoctor): Response<PostDoctor>
}