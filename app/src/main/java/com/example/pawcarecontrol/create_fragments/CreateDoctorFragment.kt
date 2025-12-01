package com.example.pawcarecontrol.create_fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.lifecycleScope
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

    private lateinit var doctorClient: DoctorClient

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
            root.findViewById<TextView>(R.id.tvTitle).text = "Editar registro doctor"
        }

        val btnAddDoctor= root.findViewById<Button>(R.id.btnAddDoctor)
        val btnCancelDoctor = root.findViewById<Button>(R.id.btnCancelDoctor)

        btnAddDoctor.setOnClickListener{
            val firstName = binding.inputFirstName.text?.toString().orEmpty()
            val lastName  = binding.inputLastName.text?.toString().orEmpty()
            val email     = binding.inputEmail.text?.toString().orEmpty()
            val password  = binding.inputPass.text?.toString().orEmpty()
            val gender    = binding.autoCompleteGenders.text?.toString().orEmpty()

            if (firstName.isEmpty() || lastName.isEmpty() || password.isEmpty() || gender.isEmpty() || email.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor complete todos los campos.", Toast.LENGTH_LONG).show()
            } else {
                val doctor = PostDoctor(
                    firstName, lastName, password, email,
                    1, UserType(1, 2, "Veterinario")
                )
                if (args.DoctorID == -1) createDoctor(doctor) else updateDoctor(doctor)
            }
        }

        btnCancelDoctor.setOnClickListener{
            findNavController().navigate(R.id.action_createDoctorFragment_to_listDoctorsFragment)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doctorClient = DoctorClient(requireContext())

        if (args.DoctorID != -1) {
            getDoctor { doctor ->
                doctor?.let {
                    binding.inputFirstName.setText(it.nombres)
                    binding.inputLastName.setText(it.apellidos)
                    binding.inputEmail.setText(it.correo)
                    binding.inputPass.setText(it.pass)
                    binding.autoCompleteGenders.setText(it.genero, false)
                } ?: run {
                    Toast.makeText(requireContext(), "No se pudo obtener la información del doctor", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun createDoctor(doctor: PostDoctor) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            try {
                val created = doctorClient.service.createDoctor(doctor) // suspend
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Usuario guardado exitosamente", Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_createDoctorFragment_to_listDoctorsFragment)
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) { Toast.makeText(requireContext(), "Error del servidor: ${e.message}", Toast.LENGTH_LONG).show() }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) { Toast.makeText(requireContext(), "Error de red. Por favor, revise su conexión.", Toast.LENGTH_LONG).show() }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("CreateDoctorFragment", "Error desconocido: ${e.message}", e)
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun updateDoctor(doctor: PostDoctor) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = doctorClient.service.updateDoctor(args.DoctorID, doctor) // suspend
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Usuario guardado exitosamente", Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_createDoctorFragment_to_listDoctorsFragment)
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) { Toast.makeText(requireContext(), "Error del servidor: ${e.message}", Toast.LENGTH_LONG).show() }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) { Toast.makeText(requireContext(), "Error de red. Por favor, revise su conexión.", Toast.LENGTH_LONG).show() }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("CreateDoctorFragment", "Error desconocido: ${e.message}", e)
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        // ⬇️ alternativa si tus endpoints devuelven Call<...>:
        /*
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = doctorClient.service.updateDoctor(args.DoctorID, doctor).execute()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "Usuario guardado exitosamente", Toast.LENGTH_LONG).show()
                        findNavController().navigate(R.id.action_createDoctorFragment_to_listDoctorsFragment)
                    } else {
                        Toast.makeText(requireContext(), "Error del servidor: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: HttpException) { ... }
            catch (e: IOException) { ... }
            catch (e: Exception) { ... }
        }
        */
    }

    private fun getDoctor(callback: (Doctor?) -> Unit) {
        // si es suspend:
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            try {
                val doctor = doctorClient.service.getDoctor(args.DoctorID) // suspend
                withContext(Dispatchers.Main) { callback(doctor) }
            } catch (_: Exception) {
                withContext(Dispatchers.Main) { callback(null) }
            }
        }

    }
}