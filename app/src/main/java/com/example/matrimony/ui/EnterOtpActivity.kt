package com.example.matrimony.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import com.example.matrimony.R
import com.example.matrimony.TAG
import com.example.matrimony.databinding.ActivityEnterOtpBinding


class EnterOtpActivity : AppCompatActivity() {

    lateinit var binding: ActivityEnterOtpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_enter_otp)

        otpTextChangeListener()
        binding.btnVerifyOtp.setOnClickListener {
            verifyOtp()
        }
    }

    private fun verifyOtp() {

        Log.i(
            TAG,
            "${intent.getStringExtra("class_name")} and ${ForgotPasswordActivity::class.simpleName}"
        )
        if (intent.getStringExtra("class_name") != ForgotPasswordActivity::class.simpleName)
            return

        val intent = Intent(this, ResetPasswordActivity::class.java)
        startActivity(intent)
    }

    private fun otpTextChangeListener() {
        val etOtp1 = binding.etOtp1
        val etOtp2 = binding.etOtp2
        val etOtp3 = binding.etOtp3
        val etOtp4 = binding.etOtp4
        val etOtp5 = binding.etOtp5
        val etOtp6 = binding.etOtp6

        val otpEditTexts = ArrayList<EditText>().apply {
            add(findViewById(R.id.et_otp_1))
            add(findViewById(R.id.et_otp_2))
            add(findViewById(R.id.et_otp_3))
            add(findViewById(R.id.et_otp_4))
            add(findViewById(R.id.et_otp_5))
            add(findViewById(R.id.et_otp_6))
        }

        val textChangedListener = object : TextWatcher {
            var tempString: String? = null
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                for (i in 0 until otpEditTexts.size) {
                    if (s == otpEditTexts[i].editableText) {
                        if (s?.isBlank() == true)
                            return
                        else if (s.toString().length >= 2 && i != otpEditTexts.size - 1) {
                            otpEditTexts[i].setText(s.toString()[0].toString())
                            tempString = s.toString().substring(1, s!!.length)
                            otpEditTexts[i + 1].setText(tempString)
                        } else if (i != otpEditTexts.size - 1)
                            otpEditTexts[i + 1].requestFocus()
                        if (i == otpEditTexts.size - 1)
                            otpEditTexts[i].setSelection(1)
                    }
                }
            }
        }

        for (i in 0 until otpEditTexts.size) {
            otpEditTexts[i].addTextChangedListener(textChangedListener)
            otpEditTexts[i].setOnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN && i != 0) {
                    otpEditTexts[i - 1].requestFocus()
                    otpEditTexts[i - 1].setSelection(otpEditTexts[i - 1].length())
                }
                false
            }
        }
    }
}