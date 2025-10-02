package com.example.pawcarecontrol.Helpers

import android.os.Bundle
import android.widget.*
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DatePickerHelper (
    private val input: TextView,
    private val supportFragmentManager: FragmentManager){

    init {
        input.setOnClickListener {
            showDatePicker(input, supportFragmentManager)
        }
    }

    private fun showDatePicker(input: TextView, supportFragmentManager: FragmentManager) {
        val builder = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Seleccionar Fecha")
        val picker = builder.build()

        picker.addOnPositiveButtonClickListener { selection ->
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            input.text = dateFormat.format(Date(selection))
        }

        picker.show(supportFragmentManager, picker.toString())
    }

}