package com.example.softengdevtest

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class Summary : AppCompatActivity() {
    private var db = Firebase.firestore
    private lateinit var leaveNum : TextView
    private lateinit var leaveType : TextView
    private lateinit var dateOfLeave : TextView
    private var userId : String = "Something"
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)
        val username = intent.getStringExtra("username")
        userId = intent.getStringExtra("id").toString()
        val userName = findViewById<TextView>(R.id.username)
        userName.text = username.toString()
        leaveNum = findViewById(R.id.leaveNum)
        leaveType = findViewById(R.id.leaveType)
        dateOfLeave = findViewById(R.id.dateOfLeave)

        val ref = db.collection("Leave Applicants").document(userId).collection("Leave Application")
        ref.get().addOnSuccessListener {querySnapshot ->
            val documentCount = querySnapshot.size()
            getLeaveData(documentCount)
        }.addOnFailureListener {
            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show()
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
}