package com.example.matrimony.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.example.matrimony.R
import com.example.matrimony.TAG
import com.example.matrimony.databinding.ActivitySignUpBinding
import com.example.matrimony.utils.Validator
import com.google.android.material.textfield.TextInputLayout

class SignUpActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignUpBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_sign_up)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)

        binding.btnContinue.setOnClickListener {
            continueNextPage()
        }

        binding.tvLogin.setOnClickListener {
            goToLoginPage()
        }


        binding.etDob.keyListener = null
        setListeners()
        initReligionSpinners()
        initMotherTongueSpinner()
        initStateSpinner()
        registerFocusListeners()


        binding.religionSelector.keyListener = null
        binding.casteSelector.keyListener = null
        binding.motherTongueSelector.keyListener = null
        binding.stateSelector.keyListener = null
        binding.citySelector.keyListener = null
    }


    private fun setListeners() {
        binding.imgPickDate.setOnClickListener {
            val datePicker = DatePickerFragment(true)
            datePicker.datePickerListener =
                DatePickerListener { date -> binding.etDob.setText(date) }
            datePicker.show(supportFragmentManager, "date-picker")
        }
    }

    private fun goToLoginPage() {
        super.onBackPressed()
    }

    private fun continueNextPage() {
        if (isPageFilled()) {
            val intent = Intent(this, SignUpNextPageActivity::class.java)
            startActivity(intent)
        }
    }

    private fun registerFocusListeners() {
        //name
        binding.etName.onFocusChangeListener = OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                (view as TextInputLayout).isErrorEnabled = false
            }
        }

        //dob
        binding.etDob.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.etDob.text?.isNotBlank() == true)
                    binding.tilDob.isErrorEnabled = false
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })


        //religion
        binding.religionSelector.setOnFocusChangeListener { v, hasFocus ->

        }



        //password
        binding.etPassword.setOnFocusChangeListener { v, hasFocus ->
            if (binding.etPassword.text?.isBlank() == true)
                return@setOnFocusChangeListener
            if (!hasFocus) {
                if (!Validator.strongPasswordValidator(binding.etPassword.text.toString())) {
                    binding.tilPassword.isErrorEnabled = true
                    binding.tilPassword.error = "Enter Strong Password"
                } else {
                    binding.tilPassword.isErrorEnabled = false
                }
            } else {
                binding.tilPassword.isErrorEnabled = false
            }
        }
    }

    private fun initReligionSpinners() {
        val religionArray = resources.getStringArray(R.array.religion)
        val religionArrayAdapter =
            object : ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                religionArray
            ) {
                override fun isEnabled(position: Int): Boolean {
                    return position != 0
                }
            }
        binding.religionSelector.setAdapter(religionArrayAdapter)

        var selectedReligion: String
        var previousPosition = -1
        binding.religionSelector.setOnItemClickListener { parent, view, position, id ->
            if (previousPosition == position)
                return@setOnItemClickListener
            previousPosition = position
            selectedReligion = parent.getItemAtPosition(position).toString()
            binding.casteSelector.setText("")
            initCasteSpinner(selectedReligion)
            Log.i(TAG, selectedReligion)
        }
    }

    private fun initCasteSpinner(selectedReligion: String) {
        val casteArray: Array<String>
        val casteArrayAdapter: ArrayAdapter<String>
        when (selectedReligion) {
            "Muslim" -> {
                casteArray = resources.getStringArray(R.array.muslim_caste)
                casteArrayAdapter =
                    ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, casteArray)

                binding.casteSelector.visibility = View.VISIBLE
                binding.tilCaste.visibility = View.VISIBLE
                binding.casteSelector.setAdapter(casteArrayAdapter)
            }
            "Hindu" -> {
                casteArray = resources.getStringArray(R.array.hindu_caste)
                casteArrayAdapter =
                    ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, casteArray)
                binding.casteSelector.visibility = View.VISIBLE
                binding.tilCaste.visibility = View.VISIBLE
                binding.casteSelector.setAdapter(casteArrayAdapter)
            }
            "Christian" -> {
                casteArray = resources.getStringArray(R.array.christian_caste)
                casteArrayAdapter =
                    ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, casteArray)
                binding.casteSelector.visibility = View.VISIBLE
                binding.tilCaste.visibility = View.VISIBLE
                binding.casteSelector.setAdapter(casteArrayAdapter)
            }
            "Atheism" -> {
                binding.casteSelector.visibility = View.GONE
                binding.tilCaste.visibility = View.GONE
            }
        }
    }

    private fun initMotherTongueSpinner() {
        val languageArray = resources.getStringArray(R.array.languages)
        val languagesArrayAdapter =
            object : ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                languageArray
            ) {
                override fun isEnabled(position: Int): Boolean {
                    return position != 0
                }
            }
        binding.motherTongueSelector.setAdapter(languagesArrayAdapter)

    }

    private fun initStateSpinner() {
        val statesArray = resources.getStringArray(R.array.state)
        val statesArrayAdapter =
            object : ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                statesArray
            ) {
                override fun isEnabled(position: Int): Boolean {
                    return position != 0
                }
            }
        binding.stateSelector.setAdapter(statesArrayAdapter)

        var selectedState: String
        var previousPosition = -1
        binding.stateSelector.setOnItemClickListener { parent, view, position, id ->
            if (previousPosition == position)
                return@setOnItemClickListener
            previousPosition = position
            selectedState = parent.getItemAtPosition(position).toString()
            binding.citySelector.setText("")
            initCitySpinner(position)
            Log.i(TAG, selectedState)
        }
    }

    private fun initCitySpinner(selectedStatePosition: Int) {
        val cityArray: Array<String>
        val cityArrayAdapter: ArrayAdapter<String>
        when (selectedStatePosition) {
            1 -> {
                cityArray = resources.getStringArray(R.array.andhra_cities)
                cityArrayAdapter =
                    ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, cityArray)

                binding.citySelector.visibility = View.VISIBLE
                binding.tilCity.visibility = View.VISIBLE
                binding.citySelector.setAdapter(cityArrayAdapter)
            }
            2 -> {
                cityArray = resources.getStringArray(R.array.karnataka_cities)
                cityArrayAdapter =
                    ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, cityArray)

                binding.citySelector.visibility = View.VISIBLE
                binding.tilCity.visibility = View.VISIBLE
                binding.citySelector.setAdapter(cityArrayAdapter)
            }
            3 -> {
                cityArray = resources.getStringArray(R.array.kerala_cities)
                cityArrayAdapter =
                    ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, cityArray)

                binding.citySelector.visibility = View.VISIBLE
                binding.tilCity.visibility = View.VISIBLE
                binding.citySelector.setAdapter(cityArrayAdapter)
            }
            4 -> {
                cityArray = resources.getStringArray(R.array.tn_cities)
                cityArrayAdapter =
                    ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, cityArray)

                binding.citySelector.visibility = View.VISIBLE
                binding.tilCity.visibility = View.VISIBLE
                binding.citySelector.setAdapter(cityArrayAdapter)
            }
            5 -> {
                binding.citySelector.visibility = View.GONE
                binding.tilCity.visibility = View.GONE
            }
        }
    }

    private fun isPageFilled(): Boolean {
        var isFilled = true
        if (binding.etName.text?.isBlank() == true) {
            isFilled = false
            binding.tilName.isErrorEnabled = true
            binding.tilName.error = "Enter full name"
        }

        return isFilled
    }
}