package com.example.pawcarecontrol.create_fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pawcarecontrol.model.Appointment.IdWrapper
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.pawcarecontrol.R
import com.example.pawcarecontrol.model.Encargado.Encargado
import com.example.pawcarecontrol.model.Encargado.EncargadoClient
import com.example.pawcarecontrol.model.Paciente.CreatePetRequest
import com.example.pawcarecontrol.model.Paciente.PacienteClient
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CreatePetFragment : Fragment() {
    // Vistas de la UI
    private lateinit var inputName: TextInputEditText
    private lateinit var inputRace: TextInputEditText
    private lateinit var btnSelectBirthday: Button
    private lateinit var tvSelectedBirthday: TextView
    private lateinit var ownerSpinner: Spinner
    private lateinit var btnAddPet: Button

    // Datos
    private var ownerList: List<Encargado> = emptyList()
    private val calendar: Calendar = Calendar.getInstance()
    private var calculatedAge: String = "" // Variable para guardar la edad calculada

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_create_pet, container, false)

        inputName = root.findViewById(R.id.inputName)
        inputRace = root.findViewById(R.id.inputRace)
        btnSelectBirthday = root.findViewById(R.id.btnSelectBirthday)
        tvSelectedBirthday = root.findViewById(R.id.tvSelectedBirthday)
        ownerSpinner = root.findViewById(R.id.ownerSpinner)
        btnAddPet = root.findViewById(R.id.btnAddPet)

        setupDatePicker()
        loadOwners()

        btnAddPet.setOnClickListener {
            createPet()
        }

        return root
    }

    private fun setupDatePicker() {
        btnSelectBirthday.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    tvSelectedBirthday.text = dateFormat.format(calendar.time)
                    tvSelectedBirthday.visibility = View.VISIBLE

                    // Calcular y GUARDAR la edad
                    calculateAndSetAge(calendar)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
            datePickerDialog.show()
        }
    }

    private fun calculateAndSetAge(birthDate: Calendar) {
        val today = Calendar.getInstance()
        var age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR)
        if (today.get(Calendar.DAY_OF_YEAR) < birthDate.get(Calendar.DAY_OF_YEAR)) {
            age--
        }
        // Guardamos la edad como un String numérico
        calculatedAge = age.toString()
    }

    private fun loadOwners() {
        lifecycleScope.launch {
            try {
                ownerList = EncargadoClient.service.getEncargados()
                val ownerNames = ownerList.map { "${it.nombres} ${it.apellidos}" }
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, ownerNames)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                ownerSpinner.adapter = adapter
            } catch (e: Exception) {
                Toast.makeText(context, "Error al cargar dueños: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createPet() {
        val name = inputName.text.toString().trim()
        val race = inputRace.text.toString().trim()
        val birthday = tvSelectedBirthday.text.toString()

        if (name.isEmpty() || race.isEmpty() || birthday.isEmpty() || ownerSpinner.selectedItemPosition < 0) {
            Toast.makeText(requireContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedOwnerId = ownerList[ownerSpinner.selectedItemPosition].id

        // Construimos el request INCLUYENDO LA EDAD
        val request = CreatePetRequest(
            nombre = name,
            raza = race,
            fecha_nacimiento = birthday,
            edad = calculatedAge, // <-- ENVIANDO LA EDAD
            encargadoMascota = IdWrapper(id = selectedOwnerId)
        )

        lifecycleScope.launch {
            try {
                val response = PacienteClient.service.createPet(request)
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Mascota registrada exitosamente", Toast.LENGTH_LONG).show()
                    findNavController().navigateUp() // Regresar a la pantalla anterior
                } else {
                    Toast.makeText(requireContext(), "Error del servidor: ${response.code()}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error de red: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
