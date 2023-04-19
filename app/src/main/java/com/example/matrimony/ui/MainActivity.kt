package com.example.matrimony.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.matrimony.R
import com.example.matrimony.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    lateinit var fragmentContainer: FrameLayout
    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

//        setContentView(R.layout.page_personal_info_edit)
        setCurrentFragment(HomePageFragment())

        fragmentContainer = binding.fragmentContainer
        bottomNavigationView = binding.bottomNavView

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    setCurrentFragment(HomePageFragment())
                }
                R.id.nav_search -> {
                    setCurrentFragment(SearchPageFragment())
                }
                R.id.nav_shortlists -> {
                    setCurrentFragment(ShortlistsPageFragment())

                }
                R.id.nav_requests -> {
                    setCurrentFragment(RequestsPageFragment())
                }
            }
            true
        }

    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
//            addToBackStack(null)
            commit()
        }
    }


    override fun onBackPressed() {
        if (!bottomNavigationView.menu.getItem(0).isChecked) {
            bottomNavigationView.menu.getItem(0).isChecked = true
            setCurrentFragment(HomePageFragment())
        } else
            super.onBackPressed()
    }
}