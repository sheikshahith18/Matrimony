package com.example.matrimony.ui.mainscreen.homescreen.settingsscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.example.matrimony.R

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        findViewById<TextView>(R.id.tv_privacy_settings).setOnClickListener {
            val intent = Intent(this, PrivacySettingsActivity::class.java)
            startActivity(intent)
        }

        findViewById<TextView>(R.id.tv_account_settings).setOnClickListener {
            val intent = Intent(this, AccountSettingsActivity::class.java)
            startActivity(intent)
        }

        setSupportActionBar(findViewById<Toolbar>(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
}