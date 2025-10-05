package com.example.pawcarecontrol.create_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.pawcarecontrol.R

class CreatePetFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_create_pet, container, false)

        val btnAddPet = root.findViewById<Button>(R.id.btnAddPet)

        btnAddPet.setOnClickListener{
            findNavController().navigate(R.id.action_createPetFragment_to_listPetsFragment)
        }

        return  root
    }
}