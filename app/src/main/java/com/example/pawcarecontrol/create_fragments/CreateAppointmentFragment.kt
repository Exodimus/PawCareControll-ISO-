package com.example.pawcarecontrol.create_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.pawcarecontrol.R

class CreateAppointmentFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_create_appointment, container, false)
        val btnAddAppointment = root.findViewById<Button>(R.id.btnAddAppointment)
        /*val mascotaSpinner: Spinner = root.findViewById(R.id.mascota_spinner)
        val doctorSpinner: Spinner = root.findViewById(R.id.doctor_spinner)

        // Define las opciones para los Spinners
        val mascotas = arrayOf("Perro", "Gato", "Pájaro", "Pez", "Conejo")
        val doctores = arrayOf("Pedro Parker", "Natalia Jimenez", "Brandon Alexis", "Alexa Quintanilla", "Alejandro Gomez")

        val mascotaAdapter = ArrayAdapter(requireActivity().applicationContext, android.R.layout.simple_spinner_item, mascotas)
        mascotaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val doctorAdapter = ArrayAdapter(requireActivity().applicationContext, android.R.layout.simple_spinner_item, doctores)
        doctorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Establece los adaptadores después de inicializarlos
        mascotaSpinner.adapter = mascotaAdapter
        doctorSpinner.adapter = doctorAdapter

        mascotaSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = mascotas[position]
                Toast.makeText(requireContext(), selectedItem, Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }*/

        btnAddAppointment.setOnClickListener {
            findNavController().navigate(R.id.action_createAppointmentFragment_to_listAppointmentsFragment)
        }


        return root
    }

}