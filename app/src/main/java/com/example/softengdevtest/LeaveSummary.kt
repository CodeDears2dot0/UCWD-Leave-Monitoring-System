package com.example.softengdevtest

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class LeaveSummary : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_leave_summary)
        findViewById<Button>(R.id.btncontinue1).setOnClickListener {
            val intent = Intent(this, Summary::class.java)
            startActivity(intent)
        }
    }
}