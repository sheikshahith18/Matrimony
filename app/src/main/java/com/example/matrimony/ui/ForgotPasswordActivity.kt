package com.example.matrimony.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.matrimony.R
import com.example.matrimony.databinding.ActivityForgotPasswordBinding


class ForgotPasswordActivity : AppCompatActivity() {
    lateinit var binding: ActivityForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this, R.layout.activity_forgot_password)


        binding.btnSendOtp.setOnClickListener {
            sendOtp()
        }
    }

    private fun sendOtp() {
        val intent=Intent(this,EnterOtpActivity::class.java)
        intent.putExtra("class_name","ForgotPasswordActivity")
        startActivity(intent)
    }
}