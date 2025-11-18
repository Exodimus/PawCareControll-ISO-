package com.example.pawcarecontrol.list_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.pawcarecontrol.Global
import com.example.pawcarecontrol.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class ListAppointmentsFragment : Fragment() {
    data class Appointment(
        val date: String,
        val doctor: String,
        val reason: String
    )

    private val Appointments = listOf(
        Appointment("2024-05-12", "Dr. García", "Consulta de rutina"),
        Appointment("2024-05-15", "Dra. López", "Control de presión arterial"),
        Appointment("2024-05-18", "Dr. Martínez", "Seguimiento de tratamiento")
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_list_appointments, container, false)

        val btnCreateAppointment = root.findViewById<ExtendedFloatingActionButton>(R.id.btnCreateAppointment)
        val layoutAppointments = root.findViewById<LinearLayout>(R.id.layoutAppointments)

        for (dataAppointment in Appointments) {
            val appointmentView = layoutInflater.inflate(R.layout.item_appointment, null)

            // Vistas del texto dentro del layout de la cita
            val textViewDate = appointmentView.findViewById<TextView>(R.id.textViewDate)
            val textViewDoctor = appointmentView.findViewById<TextView>(R.id.textViewDoctor)
            val textViewReason = appointmentView.findViewById<TextView>(R.id.textViewReason)

            textViewDate.text = getString(R.string.appointment_date_format, dataAppointment.date)
            textViewDoctor.text = getString(R.string.appointment_doctor_format, dataAppointment.doctor)
            textViewReason.text = getString(R.string.appointment_reason_format, dataAppointment.reason)


            layoutAppointments.addView(appointmentView)
        }

        btnCreateAppointment.setOnClickListener{
            findNavController().navigate(R.id.action_listAppointmentsFragment_to_createAppointmentFragment)
        }

        val bottomNavigation = root.findViewById<BottomNavigationView>(R.id.bottom_navigation)

        if(Global.userType.toString() != "Administrador") {
            bottomNavigation.menu.findItem(R.id.page_1).isVisible = false
        }
        bottomNavigation.selectedItemId = R.id.page_2

        bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.page_1 -> {
                    findNavController().navigate(R.id.action_global_doctors2)
                    true
                }

                R.id.page_2 -> {
                    true
                }

                R.id.page_3 -> {
                    findNavController().navigate(R.id.action_global_pets2)
                    true
                }

                else -> false
            }
        }



        return root
    }
}
