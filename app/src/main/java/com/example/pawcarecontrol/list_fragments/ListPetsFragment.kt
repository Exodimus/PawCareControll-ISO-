package com.example.pawcarecontrol.list_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.pawcarecontrol.Global
import com.example.pawcarecontrol.Helpers.AuthHelper
import com.example.pawcarecontrol.R
import com.example.pawcarecontrol.adapters.PetAdapter
import com.example.pawcarecontrol.model.Paciente.PacienteClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListPetsFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var recyclerViewPets: RecyclerView
    // Se inicializa el adaptador una sola vez con una lista vacía.
    private val petAdapter = PetAdapter(emptyList())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_list_pets, container, false)
        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        setupViews(root)
        setupRecyclerView(root)
        setupNavigation(root)

        return root
    }

    override fun onResume() {
        super.onResume()
        loadPets()
    }

    private fun setupViews(root: View) {
        val btnCreatePet = root.findViewById<ExtendedFloatingActionButton>(R.id.btnCreatePet)
        btnCreatePet.setOnClickListener {
            findNavController().navigate(R.id.action_listPetsFragment_to_createPetFragment)
        }
    }

    private fun setupRecyclerView(root: View) {
        recyclerViewPets = root.findViewById(R.id.recyclerViewPets)
        // Se asigna el adaptador que ya fue inicializado.
        recyclerViewPets.adapter = petAdapter
    }

    private fun loadPets() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val petsList = PacienteClient.service.getPacientes()

                withContext(Dispatchers.Main) {
                    // En lugar de crear un nuevo adaptador, actualizamos los datos del existente.
                    petAdapter.updateData(petsList)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "Error al cargar las mascotas: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun setupNavigation(root: View) {
        val bottomNavigation = root.findViewById<BottomNavigationView>(R.id.bottom_navigation)

        if (Global.userType.toString() != "Administrador") {
            bottomNavigation.menu.findItem(R.id.page_1).isVisible = false
        }
        bottomNavigation.selectedItemId = R.id.page_3

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_1 -> {
                    findNavController().navigate(R.id.action_global_doctors)
                    true
                }
                R.id.page_2 -> {
                    findNavController().navigate(R.id.action_global_appointments2)
                    true
                }
                R.id.page_3 -> true
                R.id.nav_logout -> {
                    AuthHelper.logout(requireContext(), googleSignInClient) {
                        findNavController().navigate(R.id.mainFragment)
                    }
                    true
                }
                else -> false
            }
        }
    }
}
