package com.example.pawcarecontrol.model

import android.content.Context
import io.github.cdimascio.dotenv.dotenv
import io.github.cdimascio.dotenv.Dotenv
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Properties

open class BaseClient(context: Context) {
    private val props: Properties by lazy {
        val properties = Properties()
        context.assets.open("env").use { input ->
            properties.load(input)
        }
        properties
    }

    protected val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(props.getProperty("BASE_URL"))
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}