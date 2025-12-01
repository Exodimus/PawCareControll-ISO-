package com.example.pawcarecontrol.create_fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.pawcarecontrol.R
import com.example.pawcarecontrol.model.Appointment.AppointmentClient
import com.example.pawcarecontrol.model.Appointment.CreateAppointmentRequest
import com.example.pawcarecontrol.model.Appointment.IdWrapper
import com.example.pawcarecontrol.model.Doctor.Doctor
import com.example.pawcarecontrol.model.Doctor.DoctorClient
import com.example.pawcarecontrol.model.Paciente.Paciente
import com.example.pawcarecontrol.model.Paciente.PacienteClient
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CreateAppointmentFragment : Fragment() {
    // Listas para almacenar los datos de la API
    private var pacientesList: List<Paciente> = emptyList()
    private var doctoresList: List<Doctor> = emptyList()

    // Spinners y otros componentes de la UI
    private lateinit var mascotaSpinner: Spinner
    private lateinit var doctorSpinner: Spinner
    private lateinit var btnSelectDate: Button
    private lateinit var btnSelectTime: Button
    private lateinit var tvSelectedDate: TextView
    private lateinit var tvSelectedTime: TextView

    // Variables para almacenar la selección
    private var selectedPacienteId: Int? = null
    private var selectedDoctorId: Int? = null
    private val calendar: Calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_create_appointment, container, false)

        // Inicializar vistas
        mascotaSpinner = root.findViewById(R.id.mascota_spinner)
        doctorSpinner = root.findViewById(R.id.doctor_spinner)
        btnSelectDate = root.findViewById(R.id.btn_select_date)
        btnSelectTime = root.findViewById(R.id.btn_select_time)
        tvSelectedDate = root.findViewById(R.id.tv_selected_date)
        tvSelectedTime = root.findViewById(R.id.tv_selected_time)
        val btnAddAppointment = root.findViewById<Button>(R.id.btnAddAppointment)

        // Cargar datos en los Spinners
        loadPacientes()
        loadDoctores()

        // Configurar listeners para los botones
        setupDateAndTimePickers()
        btnAddAppointment.setOnClickListener { createAppointment() }

        return root
    }

    private fun loadPacientes() {
        lifecycleScope.launch {
            try {
                pacientesList = PacienteClient.service.getPacientes()
                // Formateamos el texto para mostrar "NombreMascota (Dueño)"
                val pacienteDescriptions = pacientesList.map {
                    "${it.nombre} (${it.encargadoMascota.nombres} ${it.encargadoMascota.apellidos})"
                }
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, pacienteDescriptions)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                mascotaSpinner.adapter = adapter
            } catch (e: Exception) {
                Toast.makeText(context, "Error al cargar mascotas: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadDoctores() {
        lifecycleScope.launch {
            try {
                // ID 2 para "Veterinario"
                doctoresList = DoctorClient.service.getDoctors()
                val doctorNames = doctoresList.map { "${it.nombres} ${it.apellidos}" }
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, doctorNames)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                doctorSpinner.adapter = adapter
            } catch (e: Exception) {
                Toast.makeText(context, "Error al cargar doctores: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupDateAndTimePickers() {
        btnSelectDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    tvSelectedDate.text = dateFormat.format(calendar.time)
                    tvSelectedDate.visibility = View.VISIBLE
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        btnSelectTime.setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                requireContext(),
                { _, hourOfDay, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)
                    val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                    tvSelectedTime.text = timeFormat.format(calendar.time)
                    tvSelectedTime.visibility = View.VISIBLE
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true // Formato de 24 horas
            )
            timePickerDialog.show()
        }
    }

    private fun createAppointment() {
        // Obtenemos los IDs de los elementos seleccionados en los spinners
        selectedPacienteId = if (mascotaSpinner.selectedItemPosition >= 0) pacientesList[mascotaSpinner.selectedItemPosition].id else null
        selectedDoctorId = if (doctorSpinner.selectedItemPosition >= 0) doctoresList[doctorSpinner.selectedItemPosition].id else null

        val fecha = tvSelectedDate.text.toString()
        val hora = tvSelectedTime.text.toString()

        // Validaciones
        if (selectedPacienteId == null || selectedDoctorId == null || fecha.isEmpty() || hora.isEmpty()) {
            Toast.makeText(requireContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val request = CreateAppointmentRequest(
            fecha = fecha,
            hora = hora,
            estatus = 1,
            // Envolvemos los IDs en la clase IdWrapper
            usuario = IdWrapper(id = selectedDoctorId!!),
            paciente = IdWrapper(id = selectedPacienteId!!),
            estadoCita = IdWrapper(id = 1) // "Pendiente" por defecto
        )

        lifecycleScope.launch {
            try {
                val response = AppointmentClient.service.createAppointment(request)
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Cita creada exitosamente", Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_createAppointmentFragment_to_listAppointmentsFragment)
                } else {
                    Toast.makeText(requireContext(), "Error del servidor: ${response.code()}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error de red: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
