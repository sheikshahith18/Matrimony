package com.example.matrimony.ui.mainscreen.homescreen.profilescreen.editscreen

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Filter
import androidx.activity.viewModels
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.matrimony.R
import com.example.matrimony.TAG
import com.example.matrimony.databinding.ActivityPartnerPrefEditBinding
import com.example.matrimony.db.entities.PartnerPreferences
import com.example.matrimony.models.DropdownName
import com.example.matrimony.ui.mainscreen.UserProfileViewModel
import com.example.matrimony.ui.mainscreen.homescreen.profilescreen.PartnerPreferenceViewModel
import com.example.matrimony.utils.CURRENT_USER_ID
import com.example.matrimony.utils.MY_SHARED_PREFERENCES
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PartnerPrefEditActivity : AppCompatActivity() {

    lateinit var binding: ActivityPartnerPrefEditBinding
    private val partnerPrefViewModel by viewModels<PartnerPreferenceViewModel>()
    private val userProfileViewModel by viewModels<UserProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_partner_pref_edit)

        binding.btnSetPreferences.setOnClickListener { setPreferences() }
        binding.btnClearPreferences.setOnClickListener { cleaPreferences() }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val sharedPref =
            getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        userProfileViewModel.userId = sharedPref.getInt(CURRENT_USER_ID, -1)

        binding.ageToSelector.keyListener=null
        binding.heightToSelector.keyListener=null
        binding.casteSelector.keyListener=null
        binding.citySelector.keyListener=null

        if (!partnerPrefViewModel.loaded)
            initValues()
        else {
            partnerPrefViewModel.selectedEducations.forEach {
                addChips(binding.educationChipGroup, it, DropdownName.EDUCATION)
            }
            partnerPrefViewModel.selectedEmployedIns.forEach {
                addChips(binding.employedInChipGroup, it, DropdownName.EMPLOYED_IN)
            }
            partnerPrefViewModel.selectedOccupation.forEach {
                addChips(binding.occupationChipGroup, it, DropdownName.OCCUPATION)
            }
            if (binding.religionSelector.text.isNotBlank()) {
                initDropDownList(
                    binding.casteSelector,
                    getCasteArray(binding.religionSelector.text.toString()),
                    DropdownName.CASTE
                )
                partnerPrefViewModel.selectedCastes.forEach {
                    addChips(binding.casteChipGroup, it, DropdownName.CASTE)
                }
            }
            partnerPrefViewModel.selectedStars.forEach {
                addChips(binding.starChipGroup, it, DropdownName.STAR)
            }
            partnerPrefViewModel.selectedZodiacs.forEach {
                addChips(binding.zodiacChipGroup, it, DropdownName.ZODIAC)
            }
            if (binding.stateSelector.text.isNotBlank()) {
                if (binding.stateSelector.text.toString() != "Others") {
                    binding.citySelector.visibility = View.VISIBLE
                    binding.tvCity.visibility = View.VISIBLE
                    initDropDownList(
                        binding.citySelector,
                        getCityArray(binding.citySelector.text.toString()),
                        DropdownName.CITY
                    )
                    partnerPrefViewModel.selectedCities.forEach {
                        addChips(binding.cityChipGroup, it, DropdownName.CITY)
                    }
                } else {
                    binding.citySelector.visibility = View.GONE
                    binding.tvCity.visibility = View.GONE
                    binding.cityChipGroup.removeAllViews()
                }
            }

        }

        initDropDownList(
            binding.ageFromSelector,
            Array(22) { it + 18 }.toList(),
            DropdownName.AGE_FROM
        )

        initDropDownList(
            binding.heightFromSelector,
            resources.getStringArray(R.array.height).toMutableList().apply { removeAt(size - 1) },
            DropdownName.HEIGHT_FROM
        )
        initDropDownList(
            binding.maritalStatusSelector,
            resources.getStringArray(R.array.marital_status).toList(),
            DropdownName.MARITAL_STATUS
        )
        initDropDownList(
            binding.educationSelector,
            resources.getStringArray(R.array.education).toList(),
            DropdownName.EDUCATION
        )
        initDropDownList(
            binding.employedInSelector,
            resources.getStringArray(R.array.employed_in).toList(),
            DropdownName.EMPLOYED_IN
        )
        initDropDownList(
            binding.occupationSelector,
            resources.getStringArray(R.array.occupation).toList(),
            DropdownName.OCCUPATION
        )
//        initDropDownList(
//            binding.annualIncomeSelector,
//            resources.getStringArray(R.array.annual_income).toList(),
//            DropdownName.ANNUAL_INCOME
//        )
        initDropDownList(
            binding.religionSelector,
            resources.getStringArray(R.array.religion).toList(),
            DropdownName.RELIGION
        )
        initDropDownList(
            binding.starSelector,
            resources.getStringArray(R.array.stars).toList(),
            DropdownName.STAR
        )
        initDropDownList(
            binding.zodiacSelector,
            resources.getStringArray(R.array.zodiac).toList(),
            DropdownName.ZODIAC
        )
        initDropDownList(
            binding.stateSelector,
            resources.getStringArray(R.array.state).toList(),
            DropdownName.STATE
        )

    }


    private fun initValues() {
        partnerPrefViewModel.loaded = true
        Log.i(TAG, "initValues in PartnerPref")

        lifecycleScope.launch {
            partnerPrefViewModel.getPartnerPreference(userProfileViewModel.userId)
                .observe(this@PartnerPrefEditActivity) {
                    if (it != null) {
                        binding.ageFromSelector.setText(it.age_from.toString())
                        binding.ageToSelector.setText(it.age_to.toString().ifBlank { "" })
                        binding.heightFromSelector.setText(it.height_from)
                        binding.heightToSelector.setText(it.height_to)
                        binding.maritalStatusSelector.setText(it.marital_status ?: "")

                        it.education?.forEach { value ->
                            if (value.isNotBlank())
                                addChips(binding.educationChipGroup, value, DropdownName.EDUCATION)
                        }

                        it.employed_in?.forEach { value ->
                            if (value.isNotBlank())
                                addChips(
                                    binding.employedInChipGroup,
                                    value,
                                    DropdownName.EMPLOYED_IN
                                )
                        }

                        it.occupation?.forEach { value ->
                            if (value.isNotBlank())
                                addChips(
                                    binding.occupationChipGroup,
                                    value,
                                    DropdownName.OCCUPATION
                                )
                        }

//                        binding.annualIncomeSelector.setText(it.annual_income)

                        binding.religionSelector.setText(it.religion)

                        if (it.religion != "Atheism") {
                            binding.tilCaste.visibility = View.VISIBLE
                            binding.tvCasteHeader.visibility = View.VISIBLE
                            binding.casteSelector.visibility = View.VISIBLE
                            it.caste?.forEach { value ->
                                if (value.isNotBlank())
                                    addChips(binding.casteChipGroup, value, DropdownName.CASTE)
                            }
                            initDropDownList(
                                binding.casteSelector,
                                getCasteArray(it.religion) as List<String>?,
                                DropdownName.CASTE
                            )
                        }

                        it.star?.forEach { value ->
                            if (value.isNotBlank())
                                addChips(binding.starChipGroup, value, DropdownName.STAR)
                        }

                        it.zodiac?.forEach { value ->
                            if (value.isNotBlank())
                                addChips(binding.zodiacChipGroup, value, DropdownName.ZODIAC)
                        }

                        binding.stateSelector.setText(it.state)

                        if (it.state != "Others") {
                            binding.tvCity.visibility = View.VISIBLE
                            binding.tilCity.visibility = View.VISIBLE
                            binding.citySelector.visibility = View.VISIBLE
                            it.city?.forEach { value ->
                                if (value.isNotBlank())
                                    addChips(binding.cityChipGroup, value, DropdownName.CITY)
                            }
                            if (it.state != null)
                                initDropDownList(
                                    binding.citySelector,
                                    getCityArray(it.state!!) as List<String>,
                                    DropdownName.CASTE
                                )
                        }


                    }

                }
        }

    }

    private fun initDropDownList(
        selector: AutoCompleteTextView,
        itemArray: List<Any>?,
        key: DropdownName?
    ) {
        selector.keyListener = null
        if (itemArray == null)
            return
        val arrayAdapter = object : ArrayAdapter<Any>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            itemArray
        ) {
            override fun getFilter(): Filter {
                return object : Filter() {
                    override fun performFiltering(constraint: CharSequence?): FilterResults {
                        val filterResults = FilterResults()
                        filterResults.values = itemArray
                        filterResults.count = itemArray.size
                        return filterResults
                    }

                    override fun publishResults(
                        constraint: CharSequence?,
                        results: FilterResults?
                    ) {
                        notifyDataSetChanged()
                    }
                }
            }
        }
        selector.setAdapter(arrayAdapter)

        var previousPosition = -1
        selector.setOnItemClickListener { parent, view, position, id ->

            when (key) {
                DropdownName.AGE_FROM -> {
                    if (previousPosition == position)
                        return@setOnItemClickListener
                    previousPosition = position
                    val ageArray = Array(22) { it + 18 }.toMutableList()
                    val selectedFromAge = binding.ageFromSelector.text.toString().toInt()
                    val selectedAgeTo = binding.ageToSelector.text.toString()
                    if (selectedAgeTo.isNotBlank()) {
                        val fromIndex =
                            ageArray.indexOf(binding.ageFromSelector.text.toString().toInt())
                        val toIndex = ageArray.indexOf(selectedAgeTo.toInt())
                        if (fromIndex >= toIndex)
                            binding.ageToSelector.setText("")
                    }
                    val ageToArray = Array(40 - selectedFromAge) { it + selectedFromAge + 1 }
                    initDropDownList(
                        binding.ageToSelector,
                        ageToArray.toList(),
                        DropdownName.AGE_FROM
                    )
                }
                DropdownName.HEIGHT_FROM -> {
                    if (previousPosition == position)
                        return@setOnItemClickListener
                    previousPosition = position
                    val heightArray = resources.getStringArray(R.array.height).toMutableList()
                    val heightTo = binding.heightToSelector.text.toString()
                    if (heightTo.isNotBlank()) {
                        val fromIndex =
                            heightArray.indexOf(binding.heightFromSelector.text.toString())
                        val toIndex = heightArray.indexOf(heightTo)

                        if (fromIndex >= toIndex)
                            binding.heightToSelector.setText("")
                    }

                    heightArray.clear()
                    resources.getStringArray(R.array.height).forEachIndexed { index, value ->
                        if (index > position) {
                            heightArray.add(value)
                        }
                    }
                    initDropDownList(binding.heightToSelector, heightArray, DropdownName.HEIGHT_TO)
                }
                DropdownName.RELIGION -> {
                    if (previousPosition == position)
                        return@setOnItemClickListener
                    previousPosition = position
                    binding.casteSelector.setText("")
                    binding.casteChipGroup.removeAllViews()

                    val casteArray: List<String> =
                        getCasteArray(parent.getItemAtPosition(position).toString())
                            ?: return@setOnItemClickListener

                    binding.tvCasteHeader.visibility = View.VISIBLE
                    binding.tilCaste.visibility = View.VISIBLE
                    initDropDownList(binding.casteSelector, casteArray, DropdownName.CASTE)
                }
                DropdownName.STATE -> {
                    if (previousPosition == position)
                        return@setOnItemClickListener
                    previousPosition = position
                    binding.citySelector.setText("")
                    binding.cityChipGroup.removeAllViews()

                    val cityArray: List<String> =
                        getCityArray(parent.getItemAtPosition(position).toString())
                            ?: return@setOnItemClickListener
                    binding.tvCity.visibility = View.VISIBLE
                    binding.tilCity.visibility = View.VISIBLE
                    initDropDownList(binding.citySelector, cityArray, DropdownName.CITY)
                }
                DropdownName.EDUCATION -> {
                    addChips(
                        binding.educationChipGroup,
                        binding.educationSelector.text.toString(),
                        DropdownName.EDUCATION
                    )
                }
                DropdownName.EMPLOYED_IN -> {
                    addChips(
                        binding.employedInChipGroup,
                        binding.employedInSelector.text.toString(),
                        DropdownName.EMPLOYED_IN
                    )
                }
                DropdownName.OCCUPATION -> {
                    addChips(
                        binding.occupationChipGroup,
                        binding.occupationSelector.text.toString(),
                        DropdownName.OCCUPATION
                    )
                }
                DropdownName.CASTE -> {
                    addChips(
                        binding.casteChipGroup,
                        binding.casteSelector.text.toString(),
                        DropdownName.CASTE
                    )
                }
                DropdownName.STAR -> {
                    addChips(
                        binding.starChipGroup,
                        binding.starSelector.text.toString(),
                        DropdownName.STAR
                    )
                }
                DropdownName.ZODIAC -> {
                    addChips(
                        binding.zodiacChipGroup,
                        binding.zodiacSelector.text.toString(),
                        DropdownName.ZODIAC
                    )
                }
                DropdownName.CITY -> {
                    addChips(
                        binding.cityChipGroup,
                        binding.citySelector.text.toString(),
                        DropdownName.CITY
                    )
                }
                else -> return@setOnItemClickListener
            }
        }
    }

    private fun getCasteArray(selectedReligion: String?): List<String>? {
        return when (selectedReligion) {
            "Muslim" -> resources.getStringArray(R.array.muslim_caste).toList()
            "Hindu" -> resources.getStringArray(R.array.hindu_caste).toList()
            "Christian" -> resources.getStringArray(R.array.christian_caste)
                .toList()
            "Atheism" -> {
                binding.tvCasteHeader.visibility = View.GONE
                binding.tilCaste.visibility = View.GONE
                null
            }

            else -> null
        }
    }

    private fun getCityArray(selectedState: String): List<String>? {
        return when (selectedState) {
            "Andhra Pradesh" -> resources.getStringArray(R.array.andhra_cities).toList()
            "Karnataka" -> resources.getStringArray(R.array.karnataka_cities).toList()
            "Kerala" -> resources.getStringArray(R.array.kerala_cities).toList()
            "Tamilnadu" -> resources.getStringArray(R.array.tn_cities).toList()
            "Others" -> {
                binding.tvCity.visibility = View.GONE
                binding.tilCity.visibility = View.GONE
                null
            }
            else -> null
        }
    }

    private fun addChips(chipGroup: ChipGroup, value: String, key: DropdownName?) {
        chipGroup.children.forEach {
            val chip = it as Chip
            if (chip.text == value)
                return
        }
        when (key) {
            DropdownName.EDUCATION -> partnerPrefViewModel.selectedEducations.add(value)
            DropdownName.EMPLOYED_IN -> partnerPrefViewModel.selectedEmployedIns.add(value)
            DropdownName.OCCUPATION -> partnerPrefViewModel.selectedOccupation.add(value)
            DropdownName.CASTE -> partnerPrefViewModel.selectedCastes.add(value)
            DropdownName.STAR -> partnerPrefViewModel.selectedStars.add(value)
            DropdownName.ZODIAC -> partnerPrefViewModel.selectedZodiacs.add(value)
            DropdownName.CITY -> partnerPrefViewModel.selectedCities.add(value)
            else -> return
        }
        chipGroup.addView(getChip(value, key))
        val sortedChips = chipGroup.children.toList().sortedBy { (it as Chip).text.toString() }
        chipGroup.removeAllViews()
        sortedChips.forEach { chipGroup.addView(it as Chip) }
    }

    private fun getChip(value: String, key: DropdownName?): Chip {
        return Chip(this).apply {
            text = value
            isCloseIconVisible = true
            setOnCloseIconClickListener {
//            setOnClickListener {
                (it.parent as ChipGroup).removeView(it)
                when (key) {
                    DropdownName.EDUCATION -> partnerPrefViewModel.selectedEducations.remove(value)
                    DropdownName.EMPLOYED_IN -> partnerPrefViewModel.selectedEmployedIns.remove(
                        value
                    )
                    DropdownName.OCCUPATION -> partnerPrefViewModel.selectedOccupation.remove(value)
                    DropdownName.CASTE -> partnerPrefViewModel.selectedCastes.remove(value)
                    DropdownName.STAR -> partnerPrefViewModel.selectedStars.remove(value)
                    DropdownName.ZODIAC -> partnerPrefViewModel.selectedZodiacs.remove(value)
                    DropdownName.CITY -> partnerPrefViewModel.selectedCities.remove(value)
                    else -> return@setOnCloseIconClickListener
                }
            }
        }
    }

    private fun setPreferences() {

//        lifecycleScope.launch {

        if(!isValueSet()){
            finish()
            return
        }

//        if (isValueSet()) {
        partnerPrefViewModel.addPreference(
            PartnerPreferences(
                userProfileViewModel.userId,
                binding.ageFromSelector.text.toString().ifBlank { 18 }.toString().toInt(),
                binding.ageToSelector.text.toString().ifBlank { 45 }.toString().toInt(),
                binding.heightFromSelector.text.toString().ifBlank { "4 ft 6 in" }.toString(),
                binding.heightToSelector.text.toString().ifBlank { "6 ft" }.toString(),
                binding.maritalStatusSelector.text.toString().ifBlank { null },
                partnerPrefViewModel.selectedEducations.toList().ifEmpty { null },
                partnerPrefViewModel.selectedEmployedIns.toList().ifEmpty { null },
                partnerPrefViewModel.selectedOccupation.toList().ifEmpty { null },
//                binding.annualIncomeSelector.text.toString().ifBlank { null },
                null,
                binding.religionSelector.text.toString().ifBlank { null },
                partnerPrefViewModel.selectedCastes.toList().ifEmpty { null },
                partnerPrefViewModel.selectedStars.toList().ifEmpty { null },
                partnerPrefViewModel.selectedZodiacs.toList().ifEmpty { null },
                binding.stateSelector.text.toString().ifBlank { null },
                partnerPrefViewModel.selectedCities.toList().ifEmpty { null }
            )
        )

        val sharedPref =
            getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val editor=sharedPref.edit()

        editor.putBoolean("PREFERENCE_SET",true)

        finish()
//            }
//        }


    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun isValueSet(): Boolean {

        if (binding.ageFromSelector.text.toString().isNotBlank())
            return true
        if (binding.ageToSelector.text.toString().isNotBlank())
            return true
        if (binding.heightFromSelector.text.toString().isNotBlank())
            return true
        if (binding.heightToSelector.text.toString().isNotBlank())
            return true
        if (binding.maritalStatusSelector.text.toString().isNotBlank())
            return true
        if (binding.educationSelector.text.toString().isNotBlank())
            return true
        if (binding.employedInSelector.text.toString().isNotBlank())
            return true
        if (binding.occupationSelector.text.toString().isNotBlank())
            return true
//        if (binding.annualIncomeSelector.text.toString().isNotBlank())
//            return true
        if (binding.religionSelector.text.toString().isNotBlank())
            return true
        if (binding.casteSelector.text.toString().isNotBlank())
            return true
        if (binding.starSelector.text.toString().isNotBlank())
            return true
        if (binding.zodiacSelector.text.toString().isNotBlank())
            return true
        if (binding.stateSelector.text.toString().isNotBlank())
            return true
        if (binding.citySelector.text.toString().isNotBlank())
            return true

        return false
    }


    private fun cleaPreferences() {

        binding.ageToSelector.setText("")
        binding.ageFromSelector.setText("")
        binding.heightToSelector.setText("")
        binding.heightFromSelector.setText("")
        binding.maritalStatusSelector.setText("")
        binding.educationChipGroup.removeAllViews()
        binding.employedInChipGroup.removeAllViews()
        binding.occupationChipGroup.removeAllViews()
//        binding.annualIncomeSelector.setText("")
        binding.religionSelector.setText("")
        binding.casteChipGroup.removeAllViews()
        binding.tvCasteHeader.visibility = View.GONE
        binding.tilCaste.visibility = View.GONE
        binding.starChipGroup.removeAllViews()
        binding.zodiacChipGroup.removeAllViews()
        binding.stateSelector.setText("")
        binding.tvCity.visibility = View.GONE
        binding.tilCity.visibility = View.GONE
        binding.cityChipGroup.removeAllViews()

        partnerPrefViewModel.selectedEducations.clear()
        partnerPrefViewModel.selectedEmployedIns.clear()
        partnerPrefViewModel.selectedOccupation.clear()
        partnerPrefViewModel.selectedCastes.clear()
        partnerPrefViewModel.selectedStars.clear()
        partnerPrefViewModel.selectedZodiacs.clear()
        partnerPrefViewModel.selectedCities.clear()

        partnerPrefViewModel.clearPreference(userProfileViewModel.userId)

        val sharedPref =
            getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val editor=sharedPref.edit()
        editor.putBoolean("CLEAR_PARTNER_PREF",true)
        editor.apply()

    }

}