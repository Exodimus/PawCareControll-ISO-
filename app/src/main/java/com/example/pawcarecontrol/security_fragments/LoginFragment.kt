package com.example.pawcarecontrol.security_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.pawcarecontrol.Global
import com.example.pawcarecontrol.R
import com.example.pawcarecontrol.model.User.UserClient
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class LoginFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_login, container, false)
        val btnLogin = root.findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener{
            val userEmail = root.findViewById<TextInputEditText>(R.id.inputUser).text.toString()
            val userPass = root.findViewById<TextInputEditText>(R.id.inputPass).text.toString()

            if (userEmail.isEmpty() || userPass.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor complete todos los campos.", Toast.LENGTH_LONG).show()
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val user = UserClient.service.getUserByEmailAndPass(userEmail, userPass)
                        // Actualizar la UI en el hilo principal
                        withContext(Dispatchers.Main) {
                            Toast.makeText(requireContext(), "Inicio de sesión completado", Toast.LENGTH_LONG)
                                .show()
                            Global.userType = user.tipoUsuario.nombre_Tipo_Usuario
                            if (Global.userType == "Administrador") {
                                findNavController().navigate(R.id.action_loginFragment_to_doctors)
                            } else {
                                findNavController().navigate(R.id.action_loginFragment_to_appointments)
                            }
                        }
                    }catch (e: HttpException) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(requireContext(), "Credenciales incorrectas. Por favor, inténtelo de nuevo.", Toast.LENGTH_LONG).show()
                        }
                    } catch (e: IOException) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(requireContext(), "Error de red. Por favor, revise su conexión.", Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }

            }
        }
        return root
    }
}