package com.example.matrimony.utils

import android.util.Log
import com.example.matrimony.TAG

object Validator {

    fun mobileNumValidator(mobile: String): Boolean {
        return Regex("^[6-9]\\d{9}$").matches(mobile)
    }

    fun emailAddressValidator(email: String): Boolean {
        return Regex("^\\w{5,}@gmail\\.com$").matches(email)
    }

    fun strongPasswordValidator(password: String): Boolean {
        return Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,}$")
//        val bool=Regex("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")
//            .matches(password)
//        Log.i(TAG,"$bool")
//        return Regex("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")
            .matches(password)
    }

    fun confirmPasswordValidator(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }

}