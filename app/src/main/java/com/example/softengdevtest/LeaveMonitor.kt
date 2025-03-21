package com.example.softengdevtest

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.softengdevtest.databinding.ActivityLeaveMonitorBinding
import java.text.SimpleDateFormat
import java.util.Locale

class LeaveMonitor : AppCompatActivity() {
    private lateinit var stats : TextView
    private lateinit var binding : ActivityLeaveMonitorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLeaveMonitorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = findViewById<TextView>(R.id.username)

        //Extras
        val userId = intent.getStringExtra("id")
        val user = intent.getStringExtra("username")
        val status = intent.getStringExtra("status")
        username.text = user

        // SharedPref
        val sharedPref = getSharedPreferences("myPref", MODE_PRIVATE)
        val editor = sharedPref.edit()

        if (status == null){
            stats = findViewById(R.id.stats)
            stats.text = sharedPref.getString("status", "No Leave Application")
        } else {
            stats = findViewById(R.id.stats)
            stats.text = status
            editor.apply {
                putString("status", status)
                apply()
            }
        }

        binding.exitBtn.setOnClickListener {
            val builder = AlertDialog.Builder(this)
                .setTitle("Log out")
                .setMessage("Are you sure you want to Log out?")
                .setPositiveButton("Yes") {_, _->
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
                .setNegativeButton("No") {dialog, _->
                    dialog.dismiss()
                }
            builder.create().show()
        }
        binding.recbtn.setOnClickListener {
            if (status == "No Leave Application") {
                val intent = Intent(this, LeaveApplicationActivity::class.java)
                val dateToday = Calendar.getInstance().time
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(dateToday)
                intent.putExtra("dateToday", formattedDate)
                intent.putExtra("username", user.toString())
                intent.putExtra("id", userId)
                startActivity(intent)
            }else {
                val builder = AlertDialog.Builder(this)
                    .setTitle("Leave Application")
                    .setMessage("You already submitted a leave application. Wait until the admin approves your application to submit another leave application")
                    .setPositiveButton("OK") {dialog, _->
                        dialog.dismiss()
                    }
                builder.create().show()
            }
        }
        binding.summaryBtn.setOnClickListener {
            val intent = Intent(this, Summary::class.java)
            intent.putExtra("username", user.toString())
            startActivity(intent)
        }
    }
    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        myCustomFunction()
    }

    private fun myCustomFunction() {
        val builder = AlertDialog.Builder(this)
            .setTitle("Exit Application")
            .setMessage("Are you sure you want to Exit this Application?")
            .setPositiveButton("Yes") {_, _->
                finishAffinity()
                finish()
            }
            .setNegativeButton("No") {dialog, _->
                dialog.dismiss()
            }
        builder.create().show()
    }
}