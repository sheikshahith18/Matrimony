package com.example.matrimony

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.matrimony.utils.MY_SHARED_PREFERENCES

class ClosingService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)

//        clearPreferences()

        stopSelf()
    }

    private fun clearPreferences() {
        val sharedPref = getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)

        Log.i(TAG,"service clear preference")
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

        Log.i(TAG, "clearPref filterStat ${sharedPref.getString("FILTER_STATUS", "null")}")
    }
}