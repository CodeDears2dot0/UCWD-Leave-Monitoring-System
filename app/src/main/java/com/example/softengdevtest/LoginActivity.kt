package com.example.softengdevtest

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.softengdevtest.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

   private lateinit var binding: ActivityLoginBinding

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      enableEdgeToEdge()

      binding = ActivityLoginBinding.inflate(layoutInflater)
      setContentView(binding.root)

      binding.loginbtn.setOnClickListener {
         var intent = Intent(this, LeaveMonitor::class.java)
         startActivity(intent)
      }
      binding.signUp.setOnClickListener{
         var intent = Intent(this, SignUpActivity::class.java)
         startActivity(intent)
      }
   }
}