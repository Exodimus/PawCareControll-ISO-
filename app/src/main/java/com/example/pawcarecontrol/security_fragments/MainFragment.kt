package com.example.pawcarecontrol.security_fragments

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.example.pawcarecontrol.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)
        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
        val signInButton = root.findViewById<Button>(R.id.sign_in_button)

        signInButton.setOnClickListener {
            googleSignIn();
        }

        val btnToLogin = root.findViewById<Button>(R.id.btnToLogin)

        btnToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
        }
        return root
    }

    private fun googleSignIn() {
        val signInClient = googleSignInClient.signInIntent
        launcher.launch(signInClient)
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data = result.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)
                manageResults(task)   // aquí ya sabes que fue OK
            } catch (e: ApiException) {
                Log.e("GOOGLE_SIGN_IN", "Fallo login: ${e.statusCode}", e)
            }
        }

    private fun manageResults(googleSignInTask: Task<GoogleSignInAccount>) {
        // Comprobamos si la tarea de Google Sign-In fue exitosa primero.
        if (googleSignInTask.isSuccessful) {
            val account: GoogleSignInAccount? = googleSignInTask.result
            if (account?.idToken != null) {
                // Usamos el idToken de la cuenta de Google para crear una credencial de Firebase.
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                // Inicia sesión en Firebase con la credencial.
                auth.signInWithCredential(credential)
                    .addOnCompleteListener { firebaseAuthTask -> // Renombramos 'it' a 'firebaseAuthTask' para claridad.

                        if (firebaseAuthTask.isSuccessful) {
                            // 1. Obtener el email como identificador principal.
                            val userEmail = account.displayName ?: ""

                            // 2. Guardar el email en SharedPreferences para usarlo en otros fragments.
                            val prefs = requireContext().getSharedPreferences("user_prefs", android.content.Context.MODE_PRIVATE)
                            prefs.edit()
                                .putString("username", userEmail)
                                .apply()

                            // 3. Mostrar un mensaje de éxito.
                            Toast.makeText(requireContext(), "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()

                            // 4. Navegar al siguiente destino solo si todo fue bien.
                            findNavController().navigate(R.id.action_mainFragment_to_appointments)
                        } else {
                            // El inicio de sesión en Firebase falló. Informar al usuario.
                            Toast.makeText(requireContext(), "Error de autenticación con Firebase.", Toast.LENGTH_LONG).show()
                        }
                    }
            } else {
                // Caso raro donde la cuenta no tiene un idToken.
                Toast.makeText(requireContext(), "No se pudo obtener el token de Google.", Toast.LENGTH_LONG).show()
            }
        } else {
            // La tarea inicial de Google Sign-In falló (p.ej., el usuario canceló el diálogo).
            Toast.makeText(requireContext(), "Inicio de sesión con Google cancelado o fallido.", Toast.LENGTH_LONG).show()
        }
    }

}