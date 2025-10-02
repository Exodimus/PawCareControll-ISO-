package com.example.pawcarecontrol.model.User

import com.example.pawcarecontrol.model.BaseClient

object UserClient: BaseClient() {
    val service = retrofit.create(UserService::class.java)
}