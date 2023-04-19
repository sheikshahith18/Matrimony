package com.example.matrimony.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.databinding.DataBindingUtil
import com.example.matrimony.R
import com.example.matrimony.databinding.ActivityResetPasswordBinding

class ResetPasswordActivity : AppCompatActivity() {
    lateinit var binding: ActivityResetPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reset_password)

        val passwordTextInputLayout = binding.tilConfirmPass
        val passwordTextInputEditText = binding.etConfirmPass

        passwordTextInputLayout.setEndIconOnClickListener {
            when (passwordTextInputEditText.transformationMethod) {
                is PasswordTransformationMethod -> {
                    passwordTextInputLayout.setEndIconDrawable(R.drawable.ic_baseline_eye_open)
                    passwordTextInputEditText.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                }
                is HideReturnsTransformationMethod -> {
                    passwordTextInputLayout.setEndIconDrawable(R.drawable.ic_baseline_eye_closed)
                    passwordTextInputEditText.transformationMethod =
                        PasswordTransformationMethod.getInstance()
                }
            }
        }
    }
}