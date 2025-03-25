package com.example.softengdevtest

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.softengdevtest.databinding.ActivityLeaveMonitorBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Locale

class LeaveMonitor : AppCompatActivity() {
    private lateinit var stats : TextView
    private var db = Firebase.firestore
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var binding : ActivityLeaveMonitorBinding
    private var applicationNum : Int = 0
    private var applicationNumNext : Int = 0
    private lateinit var userId : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLeaveMonitorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val username = findViewById<TextView>(R.id.username)
        stats = findViewById(R.id.stats)
        //Extras
        val user = intent.getStringExtra("username")
        userId = intent.getStringExtra("id").toString()
        username.text = user
        checkIfPending()
        // Firebase FireStore Implementation
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("Leave Applicants")

        // SharedPref
        val sharedPref = getSharedPreferences("myPref", MODE_PRIVATE)
        val editor = sharedPref.edit()
        applicationNum = sharedPref.getInt("leaveNum", 0)
        if (applicationNum == applicationNumNext){
            applicationNumNext = applicationNum + 1
            editor.apply {
                putInt("leaveNum", applicationNumNext).apply()
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
            if (stats.text == "No Leave Application" || stats.text == "null") {
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
            intent.putExtra("id", userId)
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
    @SuppressLint("SetTextI18n")
    private fun checkIfPending(){
        val ref = db.collection("Leave Applicants").document(userId).collection("Leave Application").document("Leave $applicationNum")
        ref.get().addOnSuccessListener {
            if (it.exists()) {
                val status = it.getString("applicationStatus").toString()
                stats.text = status
            }
            else {
                Toast.makeText(this, "No Leave Application", Toast.LENGTH_LONG).show()
                stats.text = "No Leave Application"
            }
        }


    }
}