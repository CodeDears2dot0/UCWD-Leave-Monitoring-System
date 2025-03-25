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
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)
        val username = intent.getStringExtra("username")
        val userName = findViewById<TextView>(R.id.username)
        val userId = intent.getStringExtra("id")
        userName.text = username.toString()

        val ref = db.collection("Leave Applicants").document(userId.toString()).collection("Leave Requests")
        ref.get().addOnSuccessListener {documents ->

        }.addOnFailureListener { exception ->
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        }
    }
}