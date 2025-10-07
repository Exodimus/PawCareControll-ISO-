package com.example.pawcarecontrol.model.User

import com.example.pawcarecontrol.model.BaseClient
import android.content.Context


class UserClient(context: Context) : BaseClient(context) {
    val service = retrofit.create(UserService::class.java)
}

