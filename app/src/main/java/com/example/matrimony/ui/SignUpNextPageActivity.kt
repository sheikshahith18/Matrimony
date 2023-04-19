package com.example.matrimony.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.matrimony.R
import com.example.matrimony.databinding.ActivitySignUpNextPageBinding

class SignUpNextPageActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignUpNextPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this, R.layout.activity_sign_up_next_page)
    }
}