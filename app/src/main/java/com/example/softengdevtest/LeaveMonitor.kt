package com.example.softengdevtest

import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.provider.CalendarContract
import android.widget.CalendarView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.softengdevtest.databinding.ActivityLeaveMonitorBinding
import java.text.SimpleDateFormat
import java.util.Locale

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
            val dateToday = Calendar.getInstance().time
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(dateToday)
            intent.putExtra("dateToday", formattedDate)
            startActivity(intent)
        }
        binding.summaryBtn.setOnClickListener {
            val intent = Intent(this, LeaveSummary::class.java)
            startActivity(intent)
        }

    }
}