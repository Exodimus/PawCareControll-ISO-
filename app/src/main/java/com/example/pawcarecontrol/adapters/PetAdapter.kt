package com.example.pawcarecontrol.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pawcarecontrol.R
import com.example.pawcarecontrol.model.Paciente.Paciente

class PetAdapter(private var pets: List<Paciente>) :

    RecyclerView.Adapter<PetAdapter.PetViewHolder>() {

    class PetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewPetName: TextView = itemView.findViewById(R.id.textViewPetName)
        val textViewPetOwner: TextView = itemView.findViewById(R.id.textViewPetOwner)
        val textViewPetBreed: TextView = itemView.findViewById(R.id.textViewPetBreed)
        val textViewPetAge: TextView = itemView.findViewById(R.id.textViewPetAge)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pet, parent, false)
        return PetViewHolder(view)
    }

    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
        val pet = pets[position]

        holder.textViewPetName.text = pet.nombre
        holder.textViewPetOwner.text = "Dueño: ${pet.encargadoMascota.nombres} ${pet.encargadoMascota.apellidos}"
        holder.textViewPetBreed.text = "Raza: ${pet.raza}"

        // Lógica para mostrar "año" o "años"
        val edadTexto = if (pet.edad == "1") "${pet.edad} año" else "${pet.edad} años"
        holder.textViewPetAge.text = "Edad: $edadTexto"
    }

    override fun getItemCount(): Int {
        return pets.size
    }

    fun updateData(newPets: List<Paciente>) {
        this.pets = newPets
        notifyDataSetChanged() // Notifica que todos los datos han cambiado
    }
}
