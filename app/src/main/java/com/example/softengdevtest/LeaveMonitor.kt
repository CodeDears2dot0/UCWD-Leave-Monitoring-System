package com.example.softengdevtest

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.softengdevtest.databinding.ActivityLeaveMonitorBinding
import com.example.softengdevtest.databinding.ActivityLoginBinding

class LeaveMonitor : AppCompatActivity() {
    private lateinit var binding : ActivityLeaveMonitorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLeaveMonitorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.exitBtn.setOnClickListener {
            finishAffinity()
        }
        binding.recbtn.setOnClickListener {
            val intent = Intent(this, LeaveApplicationActivity::class.java)
            startActivity(intent)

        }
        binding.summaryBtn.setOnClickListener {
            val intent = Intent(this, LeaveSummary::class.java)
            startActivity(intent)
        }

    }
}