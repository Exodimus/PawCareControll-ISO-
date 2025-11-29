package com.example.pawcarecontrol.Helpers

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth

object AuthHelper {

    fun logout(context: Context, googleClient: GoogleSignInClient, onLoggedOut: () -> Unit) {
        // Cerrar sesión en Firebase
        FirebaseAuth.getInstance().signOut()

        // Cerrar sesión en Google
        googleClient.signOut().addOnCompleteListener {
            val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            prefs.edit().clear().apply()
            onLoggedOut()
        }
    }
}
