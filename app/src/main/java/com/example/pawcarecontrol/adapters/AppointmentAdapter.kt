package com.example.pawcarecontrol.adapters // O donde esté tu adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pawcarecontrol.R
// IMPORTANTE: Asegúrate de que este import apunte al modelo correcto
import com.example.pawcarecontrol.model.Appointment.Appointment

class AppointmentAdapter(private val appointments: List<Appointment>) : // Ahora usa el modelo correcto
    RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder>() {

    class AppointmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewDate: TextView = itemView.findViewById(R.id.textViewDate)
        val textViewDoctor: TextView = itemView.findViewById(R.id.textViewDoctor)
        val textViewReason: TextView = itemView.findViewById(R.id.textViewReason)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_appointment, parent, false)
        return AppointmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val appointment = appointments[position]
        val context = holder.itemView.context

        // Formateamos la fecha para que sea más legible
        val formattedDate = appointment.fecha.split("T").firstOrNull() ?: appointment.fecha

        // Construimos los strings para mostrar
        val dateText = "Fecha: $formattedDate"
        // Aquí combinamos el nombre del usuario (doctor/recepcionista) que agendó
        val doctorText = "Agendado por: ${appointment.usuario.nombres} ${appointment.usuario.apellidos}"
        // Usamos el nombre y la raza del paciente como el "motivo"
        val reasonText = "Paciente: ${appointment.paciente.nombre} (${appointment.paciente.raza})"

        holder.textViewDate.text = dateText
        holder.textViewDoctor.text = doctorText
        holder.textViewReason.text = reasonText
    }

    override fun getItemCount() = appointments.size
}
