package com.example.softengdevtest

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class Summary : AppCompatActivity() {
    private var db = Firebase.firestore
    private lateinit var exitBtn : ImageButton
    private lateinit var summaryBtn : ImageButton
    private lateinit var recordBtn : ImageButton
    private lateinit var leaveNum : TextView
    private lateinit var leaveType : TextView
    private lateinit var dateOfLeave : TextView
    private lateinit var reqLeaves : TextView
    private lateinit var appLeaves : TextView
    private var userId : String = "Something"
    private var documentCount : Int = 0
    private var username : String = "Something"
    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)
        username = intent.getStringExtra("username").toString()
        userId = intent.getStringExtra("id").toString()
        val userName = findViewById<TextView>(R.id.username)
        userName.text = username

        // Initialized TextViews
        reqLeaves = findViewById(R.id.reqLeaves)
        appLeaves = findViewById(R.id.appLeaves)
        leaveNum = findViewById(R.id.leaveNum)
        leaveType = findViewById(R.id.leaveType)
        dateOfLeave = findViewById(R.id.dateOfLeave)
        exitBtn = findViewById(R.id.exit_bt)
        summaryBtn = findViewById(R.id.summary_bt)
        recordBtn = findViewById(R.id.recbtn)

        val ref = db.collection("Leave Applicants").document(userId).collection("Leave Application")
        ref.get().addOnSuccessListener {querySnapshot ->
            documentCount = querySnapshot.size()
            reqLeaves.text = documentCount.toString()
            returnApprovedLeaves { result ->
                if (result != null) {
                    appLeaves.text = result.toString()
                }else{
                    appLeaves.text = "0"
                }
            }
            getLeaveData(documentCount)
        }.addOnFailureListener {
            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show()
        }

        exitBtn.setOnClickListener {
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
        recordBtn.setOnClickListener {
            val reference = db.collection("Leave Applicants").document(userId).collection("Leave Application")
            reference.get().addOnSuccessListener {querySnapshot ->
                val documentCount = querySnapshot.size()
                val refer = db.collection("Leave Applicants").document(userId).collection("Leave Application").document("Leave ${documentCount - 1}")
                refer.get().addOnSuccessListener {
                    if (it.getString("applicationStatus") == "Approved"){
                        val intent = Intent(this, LeaveApplicationActivity::class.java)
                        intent.putExtra("username", username)
                        intent.putExtra("id", userId)
                        startActivity(intent)
                        finish()
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
            }
        }
        summaryBtn.setOnClickListener {
            val intent = Intent(this, LeaveMonitor::class.java)
            intent.putExtra("username", username)
            intent.putExtra("id", userId)
            startActivity(intent)
            finish()
        }
    }
    @SuppressLint("SetTextI18n")
    private fun getLeaveData(num : Int){
        for (number in num downTo 0) {
            val ref =
                db.collection("Leave Applicants").document(userId).collection("Leave Application")
                    .document("Leave $number")
            ref.get().addOnSuccessListener {
                if (it.exists()) {
                    val leaveNum = "Leave $number"
                    val leaveType = it.getString("applicationLeaveType")
                    val dateOfLeave = it.getString("applicationLeaveDate")
                    this.leaveNum.text = this.leaveNum.text.toString()  + "\n" + leaveNum + "\n"
                    this.leaveType.text = this.leaveType.text.toString() + "\n" + leaveType + "\n"
                    this.dateOfLeave.text = this.dateOfLeave.text.toString() + "\n" + dateOfLeave + "\n"
                }
            }
        }
    }
    private fun returnApprovedLeaves(callback: (Int?) -> Unit){
        var num = 0
        for (number in  documentCount downTo 0) {
            val ref = db.collection("Leave Applicants").document(userId).collection("Leave Application").document("Leave ${number - 1}")
            ref.get().addOnSuccessListener {
                val status = it.getString("applicationStatus")
                if (status == "Approved") {
                    num++
                    callback(num)
                }
            }.addOnFailureListener {
                callback(num)
            }
        }
    }
    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        val intent = Intent(this, LeaveMonitor::class.java)
        intent.putExtra("username", username)
        intent.putExtra("id", userId)
        startActivity(intent)
        finish()
    }
}