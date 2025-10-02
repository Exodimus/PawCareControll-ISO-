package com.example.pawcarecontrol.create_fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.pawcarecontrol.R
import com.example.pawcarecontrol.databinding.FragmentCreateDoctorBinding
import com.example.pawcarecontrol.model.Doctor.DoctorClient
import com.example.pawcarecontrol.model.Doctor.Doctor
import com.example.pawcarecontrol.model.Doctor.PostDoctor
import com.example.pawcarecontrol.model.User.UserType
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class CreateDoctorFragment : Fragment() {
    private lateinit var binding: FragmentCreateDoctorBinding

    private val args: CreateDoctorFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDoctor { doctor ->
            doctor?.let{
            view.findViewById<TextInputEditText>(R.id.inputFirstName).setText(doctor.nombres)
            view.findViewById<TextInputEditText>(R.id.inputLastName).setText(doctor.apellidos)
            view.findViewById<TextInputEditText>(R.id.inputEmail).setText(doctor.correo)
            view.findViewById<TextInputEditText>(R.id.inputPass).setText(doctor.pass)
            view.findViewById<AutoCompleteTextView>(R.id.autoCompleteGenders).setText(doctor.genero)
            }?: run {
                Toast.makeText(requireContext(), "No se pudo obtener la información del doctor", Toast.LENGTH_LONG).show()
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateDoctorBinding.inflate(inflater, container, false)
        val root = binding.root

        val genders = resources.getStringArray(R.array.genders)

        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, genders)
        binding.autoCompleteGenders.setAdapter(arrayAdapter)

        if (args.DoctorID != -1) {
            root.findViewById<TextView>(R.id.tvTitle).setText("Editar registro doctor")
        }

        val btnAddDoctor= root.findViewById<Button>(R.id.btnAddDoctor)
        val btnCancelDoctor = root.findViewById<Button>(R.id.btnCancelDoctor)

        btnAddDoctor.setOnClickListener{
            val firstName = root.findViewById<TextInputEditText>(R.id.inputFirstName).text.toString()
            val lastName = root.findViewById<TextInputEditText>(R.id.inputLastName).text.toString()
            val email = root.findViewById<TextInputEditText>(R.id.inputEmail).text.toString()
            val password = root.findViewById<TextInputEditText>(R.id.inputPass).text.toString()
            val gender = root.findViewById<AutoCompleteTextView>(R.id.autoCompleteGenders).text.toString()

            if (firstName.isEmpty()
                || lastName.isEmpty()
                || password.isEmpty()
                || gender.isEmpty()
                || email.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor complete todos los campos.", Toast.LENGTH_LONG).show()
            } else {
                if (args.DoctorID == -1) {
                    val doctor = PostDoctor(
                        firstName,
                        lastName,
                        password,
                        email,
                        1,
                        UserType(1,2, "Veterinario"))
                    createDoctor(doctor)
                } else {
                    val doctor = PostDoctor(
                        firstName,
                        lastName,
                        password,
                        email,
                        1,
                        UserType(1,2, "Veterinario"))
                    updateDoctor(doctor)
                }
            }
        }

        btnCancelDoctor.setOnClickListener{
            findNavController().navigate(R.id.action_createDoctorFragment_to_listDoctorsFragment)
        }

        return root
    }

    private fun createDoctor(doctor: PostDoctor) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response: Response<PostDoctor> = DoctorClient.service.createDoctor(doctor).execute()

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "Usuario guardado exitosamente", Toast.LENGTH_LONG).show()

                        findNavController().navigate(R.id.action_createDoctorFragment_to_listDoctorsFragment)
                    } else {
                        Toast.makeText(requireContext(), "Error del servidor: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error del servidor: ${e.message}", Toast.LENGTH_LONG).show()
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error de red. Por favor, revise su conexión.", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("CreateDoctorFragment", "Error desconocido: ${e.message}", e)
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun updateDoctor(doctor: PostDoctor) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = DoctorClient.service.updateDoctor(args.DoctorID, doctor)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "Usuario guardado exitosamente", Toast.LENGTH_LONG).show()
                        findNavController().navigate(R.id.action_createDoctorFragment_to_listDoctorsFragment)
                    } else {
                        Toast.makeText(requireContext(), "Error del servidor: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error del servidor: ${e.message}", Toast.LENGTH_LONG).show()
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error de red. Por favor, revise su conexión.", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("CreateDoctorFragment", "Error desconocido: ${e.message}", e)
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun getDoctor(callback: (Doctor?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val doctor = DoctorClient.service.getDoctor(args.DoctorID)
                withContext(Dispatchers.Main) {
                    callback(doctor)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    callback(null)
                }
            }
        }
    }
}