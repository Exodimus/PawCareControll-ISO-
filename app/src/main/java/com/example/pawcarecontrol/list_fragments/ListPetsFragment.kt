package com.example.pawcarecontrol.list_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.pawcarecontrol.Global
import com.example.pawcarecontrol.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class ListPetsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_list_pets, container, false)

        val btnCreatePet = root.findViewById<ExtendedFloatingActionButton>(R.id.btnCreatePet)

        btnCreatePet.setOnClickListener{
            findNavController().navigate(R.id.action_listPetsFragment_to_createPetFragment)
        }

        val bottomNavigation = root.findViewById<BottomNavigationView>(R.id.bottom_navigation)

        if(Global.userType.toString() != "Administrador") {
            bottomNavigation.menu.findItem(R.id.page_1).isVisible = false
        }

        bottomNavigation.selectedItemId = R.id.page_3

        bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.page_1 -> {
                    findNavController().navigate(R.id.action_global_doctors)
                    true
                }

                R.id.page_2 -> {
                    findNavController().navigate(R.id.action_global_appointments2)
                    true
                }

                R.id.page_3 -> {
                    true
                }

                else -> false
            }
        }

        return root
    }
}