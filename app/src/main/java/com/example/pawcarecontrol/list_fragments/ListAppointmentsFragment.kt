package com.example.pawcarecontrol.list_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.pawcarecontrol.Global
import com.example.pawcarecontrol.Helpers.AuthHelper
import com.example.pawcarecontrol.R
import com.example.pawcarecontrol.adapters.AppointmentAdapter
import com.example.pawcarecontrol.model.Appointment.AppointmentClient
import com.example.pawcarecontrol.model.Appointment.Appointment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListAppointmentsFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var recyclerViewAppointments: RecyclerView
    // El adaptador ahora se inicializa con una lista vacía
    private lateinit var appointmentAdapter: AppointmentAdapter

    // Se elimina la lista de datos hardcodeada

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_list_appointments, container, false)
        auth = FirebaseAuth.getInstance()

        // Configuración de Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        // Configuración de las vistas
        setupViews(root)
        // Configuración del RecyclerView con una lista vacía inicialmente
        setupRecyclerView(root)
        // Configuración de la navegación
        setupNavigation(root)
        // Cargar los datos desde la API
        loadAppointments()

        return root
    }


    private fun setupViews(root: View) {
        val lblUser = root.findViewById<TextView>(R.id.txtUser)

        // 1. Obtener la referencia a SharedPreferences
        val prefs = requireContext().getSharedPreferences("user_prefs", android.content.Context.MODE_PRIVATE)

        // 2. Leer el valor de "username". Si no se encuentra, se usa un string vacío.
        val userName = prefs.getString("username", "")
        lblUser.text = getString(R.string.user_label, userName)

        // Configuración del botón para crear una nueva cita
        val btnCreateAppointment = root.findViewById<ExtendedFloatingActionButton>(R.id.btnCreateAppointment)
        btnCreateAppointment.setOnClickListener {
            findNavController().navigate(R.id.action_listAppointmentsFragment_to_createAppointmentFragment)
        }
    }


    private fun setupRecyclerView(root: View) {
        recyclerViewAppointments = root.findViewById(R.id.recyclerViewAppointments)
        // Inicializamos el adaptador con una lista vacía.
        // Se llenará cuando los datos de la API lleguen.
        appointmentAdapter = AppointmentAdapter(emptyList())
        recyclerViewAppointments.adapter = appointmentAdapter
    }

    /**
     * Inicia una corrutina para obtener las citas desde la API.
     */
    private fun loadAppointments() {
        // Usamos lifecycleScope para que la corrutina se cancele automáticamente
        // si el fragmento se destruye, evitando memory leaks.
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // 1. Llamada a la API en un hilo de fondo (Dispatchers.IO)
                val response: List<Appointment> = AppointmentClient.service.getAppointments()

                // 2. Cambiamos al hilo principal para actualizar la UI
                withContext(Dispatchers.Main) {
                    // Creamos una nueva instancia del adaptador con los datos recibidos
                    // y la asignamos al RecyclerView.
                    appointmentAdapter = AppointmentAdapter(response)
                    recyclerViewAppointments.adapter = appointmentAdapter
                }
            } catch (e: Exception) {
                // 3. Manejo de errores
                withContext(Dispatchers.Main) {
                    // Mostramos un mensaje de error al usuario en el hilo principal
                    Toast.makeText(
                        context,
                        "Error al cargar las citas: ${e.message}",
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
        bottomNavigation.selectedItemId = R.id.page_2

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_1 -> {
                    findNavController().navigate(R.id.action_global_doctors2)
                    true
                }
                R.id.page_2 -> true // Ya estás aquí
                R.id.page_3 -> {
                    findNavController().navigate(R.id.action_global_pets2)
                    true
                }
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
