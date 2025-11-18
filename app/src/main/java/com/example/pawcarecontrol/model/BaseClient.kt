package com.example.pawcarecontrol.model

import io.github.cdimascio.dotenv.dotenv
import io.github.cdimascio.dotenv.Dotenv
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class BaseClient {
    private val dotenv: Dotenv = dotenv{
        directory = "./assets"
        filename = "env" // instead of '.env', use 'env'
    }

    protected val retrofit = Retrofit.Builder()
        .baseUrl(dotenv["BASE_URL"])
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}