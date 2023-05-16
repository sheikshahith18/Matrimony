package com.example.matrimony.utils

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    lateinit var timePickerListener:TimePickerListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        return TimePickerDialog(activity, this, hour, minute, false)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val str="$hourOfDay : $minute"
        timePickerListener.time(str)
    }
}

fun interface TimePickerListener{
    fun time(data:String)
}