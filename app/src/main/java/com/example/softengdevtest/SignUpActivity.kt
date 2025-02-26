package com.example.softengdevtest

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.softengdevtest.databinding.ActivityLoginBinding
import com.example.softengdevtest.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
   private lateinit var binding : ActivitySignUpBinding
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      enableEdgeToEdge()
      binding = ActivitySignUpBinding.inflate(layoutInflater)
      setContentView(binding.root)

   }
}