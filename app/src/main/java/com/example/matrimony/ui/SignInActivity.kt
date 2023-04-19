package com.example.matrimony.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.databinding.DataBindingUtil
import com.example.matrimony.R
import com.example.matrimony.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {

    private lateinit var signInBinding: ActivitySignInBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        signInBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)

        val passwordTextInputLayout = signInBinding.tilPassword
        val passwordTextInputEditText = signInBinding.etPassword

//        passwordTextInputLayout.setEndIconOnClickListener {
//            when (passwordTextInputEditText.transformationMethod) {
//                is PasswordTransformationMethod -> {
//                    passwordTextInputLayout.setEndIconDrawable(R.drawable.ic_baseline_eye_open)
//                    passwordTextInputEditText.transformationMethod =
//                        HideReturnsTransformationMethod.getInstance()
//                }
//                is HideReturnsTransformationMethod -> {
//                    passwordTextInputLayout.setEndIconDrawable(R.drawable.ic_baseline_eye_closed)
//                    passwordTextInputEditText.transformationMethod =
//                        PasswordTransformationMethod.getInstance()
//                }
//            }
//        }

        signInBinding.tvForgotPassword.setOnClickListener {
            forgotPassword()
        }

        signInBinding.tvLoginViaOtp.setOnClickListener {
            loginViaOTP()
        }

        signInBinding.tvCreateNewAcc.setOnClickListener {
            createNewAccount()
        }

        signInBinding.btnSignIn.setOnClickListener {
            validateSignIn()
        }
    }

    private fun forgotPassword() {
        val intent = Intent(this, ForgotPasswordActivity::class.java)
        startActivity(intent)
    }

    private fun loginViaOTP() {
        val intent = Intent(this, LoginViaOTPActivity::class.java)
        startActivity(intent)
    }

    private fun createNewAccount() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    private fun validateSignIn() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}