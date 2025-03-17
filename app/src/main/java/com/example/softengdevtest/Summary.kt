package com.example.softengdevtest

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Summary : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)
        val username = intent.getStringExtra("username")
        val userName = findViewById<TextView>(R.id.username)
        userName.text = username.toString()
    }
}