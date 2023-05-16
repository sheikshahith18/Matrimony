package com.example.matrimony.ui.mainscreen

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.matrimony.ClosingService
import com.example.matrimony.R
import com.example.matrimony.TAG
import com.example.matrimony.databinding.ActivityMainBinding
import com.example.matrimony.ui.mainscreen.connectionsscreen.ConnectionsPageFragment
import com.example.matrimony.ui.mainscreen.connectionsscreen.ConnectionsViewModel
import com.example.matrimony.ui.mainscreen.homescreen.HomePageFragment
import com.example.matrimony.ui.mainscreen.meetingscreen.MeetingsPageFragment
import com.example.matrimony.ui.mainscreen.searchscreen.SearchPageFragment
import com.example.matrimony.ui.mainscreen.shortlistsscreen.ShortlistsPageFragment
import com.example.matrimony.utils.CURRENT_USER_GENDER
import com.example.matrimony.utils.CURRENT_USER_ID
import com.example.matrimony.utils.MY_SHARED_PREFERENCES
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        private const val SELECTED_ITEM = "SELECTED_ITEM_ID"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var fragmentContainer: FrameLayout
    lateinit var bottomNavigationView: BottomNavigationView

    private val userProfileViewModel by viewModels<UserProfileViewModel>()
    private val connectionsViewModel by viewModels<ConnectionsViewModel>()

    private var selectedItemId = 0

    private val homePageFragment = HomePageFragment()
    private val searchPageFragment = SearchPageFragment()
    private val shortlistsPageFragment = ShortlistsPageFragment()
    private val connectionsPageFragment = ConnectionsPageFragment()
    private val meetingsPageFragment = MeetingsPageFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        fragmentContainer = binding.fragmentContainer
//        setBottomNavView()

        setBottomNavView()
        init()
//        if(userProfileViewModel.currentNavItem==R.id.nav_home)
//        setCurrentFragment(HomePageFragment())
    }

    private fun init() {
        val sharedPref =
            getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE) ?: return
        val userId = sharedPref.getInt(CURRENT_USER_ID, -1)

        lifecycleScope.launch {
//            if (userId != -1) {

            if (connectionsViewModel.isRequestsPending(userId)) {
                if (userProfileViewModel.initialLogin) {
                    bottomNavigationView.getOrCreateBadge(R.id.nav_connections).apply {
                        isVisible = true
                    }
                    userProfileViewModel.initialLogin = false
                }
            }

            val gender = userProfileViewModel.getUserGender(userId)
            Log.i(TAG, "Inside MainActivity currentUserId=$userId")
            Log.i(TAG, "Inside MainActivity currentGender=$gender")
            val editor = sharedPref.edit()
            editor.putString(CURRENT_USER_GENDER, gender)
            editor.apply()

            setCurrentMenuItem(userProfileViewModel.currentNavItem)
            registerNavChangeListener()

//            }
        }

    }


    private fun registerNavChangeListener() {
        binding.bottomNavView.setOnItemReselectedListener { menuItem ->
//            val sharedPref =
//                getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
//            if (sharedPref.getBoolean("CLEAR_FILTER", false)) {
//                val editor = sharedPref.edit()
//                editor.remove("CLEAR_FILTER")
//                editor.apply()
//                clearFilters()
//                loadSearchProfiles()
//            }
//            if (menuItem.itemId != R.id.nav_search) {
//                val sharedPref =
//                    getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
//                if (sharedPref.getBoolean("PREF_FILTER_APPLIED", false)
//                    || sharedPref.getBoolean("BASED_ON_CITY_APPLIED", false)
//                    || sharedPref.getBoolean("BASED_ON_EDUCATION_APPLIED", false)
//                    || sharedPref.getBoolean("BASED_ON_WORK_APPLIED", false)
//                ) {
//                    val editor = sharedPref.edit()
//                    editor.remove("PREF_FILTER_APPLIED")
//                    editor.remove("BASED_ON_CITY_APPLIED")
//                    editor.remove("BASED_ON_EDUCATION_APPLIED")
//                    editor.remove("BASED_ON_WORK_APPLIED")
//                    editor.putBoolean("FILTER_CLEARED",true)
//                    editor.apply()
//                    clearFilters()
//                }
//            }
            setCurrentMenuItem(menuItem.itemId)
        }
    }

    private fun setBottomNavView() {
        bottomNavigationView = binding.bottomNavView




        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            if (menuItem.itemId != R.id.nav_search) {
                val sharedPref =
                    getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
                if (sharedPref.getBoolean("PREF_FILTER_APPLIED", false)
                    || sharedPref.getBoolean("BASED_ON_CITY_APPLIED", false)
                    || sharedPref.getBoolean("BASED_ON_EDUCATION_APPLIED", false)
                    || sharedPref.getBoolean("BASED_ON_OCCUPATION_APPLIED", false)
                ) {
                    val editor = sharedPref.edit()
                    editor.remove("PREF_FILTER_APPLIED")
                    editor.remove("BASED_ON_CITY_APPLIED")
                    editor.remove("BASED_ON_EDUCATION_APPLIED")
                    editor.remove("BASED_ON_OCCUPATION_APPLIED")
                    editor.putBoolean("FILTER_CLEARED", true)
                    editor.apply()
                    clearFilters()
                }
            }
            if (menuItem.itemId == R.id.nav_connections) {
                bottomNavigationView.getOrCreateBadge(R.id.nav_connections).apply {
                    isVisible = false
                }
            }
            setCurrentMenuItem(menuItem.itemId)
            true
        }
    }

    private fun setCurrentMenuItem(itemId: Int) {

        when (itemId) {
            R.id.nav_home -> {
                selectedItemId = R.id.nav_home
                userProfileViewModel.currentNavItem = R.id.nav_home
                setCurrentFragment(homePageFragment)
            }
            R.id.nav_search -> {
                selectedItemId = R.id.nav_search
                userProfileViewModel.currentNavItem = R.id.nav_search
//                if(supportFragmentManager.backStackEntryCount>1){
//                    supportFragmentManager.popBackStack()
//                }
                setCurrentFragment(searchPageFragment)
            }
            R.id.nav_shortlists -> {
//                if(supportFragmentManager.backStackEntryCount>1){
//                    supportFragmentManager.popBackStack()
//                }
                userProfileViewModel.currentNavItem = R.id.nav_shortlists
                selectedItemId = R.id.nav_shortlists
                setCurrentFragment(shortlistsPageFragment)

            }
            R.id.nav_connections -> {
//                if(supportFragmentManager.backStackEntryCount>1){
//                    supportFragmentManager.popBackStack()
//                }
                userProfileViewModel.currentNavItem = R.id.nav_connections
                selectedItemId = R.id.nav_connections
                setCurrentFragment(connectionsPageFragment)
            }
            R.id.nav_meetings -> {
                selectedItemId = R.id.nav_meetings
                userProfileViewModel.currentNavItem = R.id.nav_meetings
                setCurrentFragment(meetingsPageFragment)
            }
        }
    }


    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            addToBackStack(null)
            commit()
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(SELECTED_ITEM, selectedItemId)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        selectedItemId = savedInstanceState.getInt(SELECTED_ITEM)
        Log.i(TAG+"1","OnRestore Main Act selectedItemId = $selectedItemId")
        bottomNavigationView.selectedItemId = selectedItemId
    }


    override fun onBackPressed() {
//        if (!bottomNavigationView.menu.getItem(0).isChecked) {
//            bottomNavigationView.menu.getItem(0).isChecked = true
//            setCurrentFragment(homePagFragment)
//        } else
//            super.onBackPressed()
        val homeFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) is HomePageFragment

        if (!homeFragment) {
            // navigate to home fragment
            bottomNavigationView.selectedItemId = R.id.nav_home
        } else {
            // exit the activity
            finish()
        }
    }


    fun navigateBottomView(viewId: Int) {
        Log.i(TAG, "navigate")

        when (viewId) {
            R.id.menu_shortlisted_profiles -> {
                binding.bottomNavView.selectedItemId = R.id.nav_shortlists
                setCurrentFragment(shortlistsPageFragment)
//                binding.bottomNavView.selectedItemId = R.id.menu_shortlisted_profiles
            }
            R.id.menu_connections -> {
                binding.bottomNavView.selectedItemId = R.id.nav_connections
                setCurrentFragment(connectionsPageFragment)
//                binding.bottomNavView.selectedItemId = R.id.menu_shortlisted_profiles
            }
            R.id.menu_meetings -> {
                binding.bottomNavView.selectedItemId = R.id.nav_meetings
                setCurrentFragment(meetingsPageFragment)
//                binding.bottomNavView.selectedItemId = R.id.menu_meetings
            }
        }
//        binding.bottomNavView.post {
//            binding.bottomNavView.setOnItemSelectedListener(null)
//            setBottomNavView()
//        }
    }

    fun inflateFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            addToBackStack(null)
            commit()
        }
    }

    override fun onStart() {
        super.onStart()


//        clearPreferences()
//        startService(Intent(this@MainActivity, ClosingService::class.java))
    }


    private fun clearFilters() {
        val sharedPref = getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)

        val previousFilterStatus = sharedPref.getString("FILTER_STATUS", "not_applied")
        val editor = sharedPref.edit()
        editor.remove("AGE_FROM_FILTER")
        editor.remove("AGE_TO_FILTER")
        editor.remove("HEIGHT_FROM_FILTER")
        editor.remove("HEIGHT_TO_FILTER")
        editor.remove("MARITAL_STATUS_FILTER")
        editor.remove("EDUCATION_FILTER")
        editor.remove("EMPLOYED_IN_FILTER")
        editor.remove("OCCUPATION_FILTER")
        editor.remove("ANNUAL_INCOME_FILTER")
        editor.remove("RELIGION_FILTER")
        editor.remove("CASTE_FILTER")
        editor.remove("STAR_FILTER")
        editor.remove("ZODIAC_FILTER")
        editor.remove("STATE_FILTER")
        editor.remove("CITY_FILTER")

        editor.putString("FILTER_STATUS", "not_applied")
        editor.apply()
//        Log.i(TAG, "clearPref filterStat ${sharedPref.getString("FILTER_STATUS", "null")}")
    }
}