package com.example.matrimony.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*
import kotlin.math.max
import kotlin.math.min

class DatePickerFragment(val isAge: Boolean) : DialogFragment(),
    DatePickerDialog.OnDateSetListener {
    lateinit var datePickerListener: DatePickerListener
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        var datePickerDialog: DatePickerDialog? = null
        if (isAge) {
            calendar.add(Calendar.YEAR, -18)
            val maxDate = calendar.timeInMillis
            calendar.add(Calendar.YEAR, -25)
            val minDate = calendar.timeInMillis
            datePickerDialog = DatePickerDialog(requireContext(), this, year, month, day)
            datePickerDialog.datePicker.maxDate = maxDate
            datePickerDialog.datePicker.minDate = minDate
        }

        return datePickerDialog!!
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {

        val strDate = if (day < 10) "0$day" else "$day"
        val strMonth = if (month < 9) "0${month + 1}" else "${month + 1}"
        val str = "$strDate/$strMonth/$year"
        datePickerListener.date(str)
    }

}


fun interface DatePickerListener {
    fun date(date: String)
}