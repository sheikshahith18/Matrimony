package com.example.matrimony.ui

import android.content.Intent
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.matrimony.R
import com.example.matrimony.databinding.ActivityLoginViaOtpBinding


class LoginViaOTPActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginViaOtpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_login_via_otp)

        binding.btnSendOtp.setOnClickListener {
            sendOtp()
        }
    }

    private fun sendOtp() {
        val intent= Intent(this,EnterOtpActivity::class.java)
        startActivity(intent)
    }
}