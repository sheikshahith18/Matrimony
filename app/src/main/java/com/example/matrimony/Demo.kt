package com.example.matrimony

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

const val TAG="Matrimony"

class DemoActivity : AppCompatActivity() {
    lateinit var spinner:Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_layout)

        spinner=findViewById(R.id.spinner)

        val dateArray=resources.getStringArray(R.array.dates)
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, dateArray)
        spinner.adapter = arrayAdapter
        spinner.onItemSelectedListener=object :OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                findViewById<EditText>(R.id.editText).setText(spinner.selectedItem.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        findViewById<EditText>(R.id.editText).setOnClickListener {
            spinner.performClick()
        }




    }
}