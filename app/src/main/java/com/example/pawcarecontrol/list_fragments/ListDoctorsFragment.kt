package com.example.pawcarecontrol.list_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawcarecontrol.R
import com.example.pawcarecontrol.adapters.DoctorsAdapter
import com.example.pawcarecontrol.model.Doctor.Doctor
import com.example.pawcarecontrol.model.Doctor.DoctorClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListDoctorsFragment : Fragment() {

    private lateinit var doctorsAdapter: DoctorsAdapter
    private lateinit var doctors: MutableList<Doctor>
    private lateinit var doctorClient: DoctorClient
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_list_doctors, container, false)
        val recyclerViewDoctors = root.findViewById<RecyclerView>(R.id.doctorsContainer)
        val btnCreateDoctor = root.findViewById<ExtendedFloatingActionButton>(R.id.btnCreateDoctor)
        val bottomNavigation = root.findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val navigationController = findNavController()

        val context = requireContext()


        doctorClient = DoctorClient(context)

        doctors = mutableListOf()
        doctorsAdapter = DoctorsAdapter(doctors, context, navigationController)
        recyclerViewDoctors.layoutManager = LinearLayoutManager(context)
        recyclerViewDoctors.adapter = doctorsAdapter

        getDoctors { doctorsList ->
            doctorsAdapter.updateDoctors(doctorsList)
        }

        btnCreateDoctor.setOnClickListener {
            navigationController.navigate(
                ListDoctorsFragmentDirections.actionListDoctorsFragmentToCreateDoctorFragment(
                    DoctorID = -1
                )
            )
        }

        bottomNavigation.selectedItemId = R.id.page_1

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_1 -> true
                R.id.page_2 -> {
                    navigationController.navigate(R.id.action_global_appointments)
                    true
                }
                R.id.page_3 -> {
                    navigationController.navigate(R.id.action_global_pets)
                    true
                }
                else -> false
            }
        }

        return root
    }

    private fun getDoctors(callback: (List<Doctor>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val doctorsList = doctorClient.service.getDoctors()
                withContext(Dispatchers.Main) {
                    doctorsAdapter.updateDoctors(doctorsList)
                    callback(doctorsList)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    callback(emptyList())
                }
            }
        }
    }
}