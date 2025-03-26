package com.example.softengdevtest

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class LeaveApplicationActivity : AppCompatActivity() {
   private var db = Firebase.firestore
   private lateinit var dbRef: DatabaseReference
   private lateinit var applicantName : TextView
   private lateinit var typeLeave : Spinner
   private lateinit var dateLeave : TextView
   private lateinit var numLeave : EditText
   private lateinit var dateOfLeave : EditText
   private lateinit var userId : String
   private lateinit var name : String
   private var leaveNo = 0
   private var initialNum = 3
   @SuppressLint("MissingInflatedId")
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContentView(R.layout.activity_leave_application01)
      //Extras
      name = intent.getStringExtra("username").toString()
      userId = intent.getStringExtra("id").toString()

      val dataToday = intent?.getStringExtra("dateToday")
      typeLeave = findViewById(R.id.spinner)

      val sharedPreferences = getSharedPreferences("myPref", MODE_PRIVATE)
      val editor = sharedPreferences.edit()

      ArrayAdapter.createFromResource(
         this,
         R.array.leaves,
         android.R.layout.simple_spinner_item
      ).also { adapter ->
         adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
         typeLeave.adapter = adapter
      }
      applicantName = findViewById(R.id.employeeName)
      dateLeave = findViewById(R.id.dateToday)
      dateLeave.text = dataToday
      numLeave = findViewById(R.id.numLeave)
      dateOfLeave = findViewById(R.id.leaveDate)
      applicantName.text = name
      dbRef = FirebaseDatabase.getInstance().getReference("Leave Applicants")

      if (initialNum >= leaveNo) {
         initialNum++
         editor.apply {
            putInt("int", initialNum)
            apply()
         }
      }
      leaveNo = sharedPreferences.getInt("int", 0)

      findViewById<Button>(R.id.savebtn).setOnClickListener {
         val builder = AlertDialog.Builder(this)
         builder.setTitle("Confirm Leave Application")
            .setMessage("Are you sure you want to submit this leave application?")
            .setPositiveButton("Yes") {_, _ ->
               saveApplicationLeave()
            }
            .setNegativeButton("No") {dialog, _ ->
               dialog.dismiss()
            }.show()
      }
   }

   private fun saveApplicationLeave(){
      val employeeName = applicantName.text.toString()
      val typeOfLeave = typeLeave.selectedItem.toString()
      val dateLeave = dateLeave.text.toString()
      val numOfLeave = numLeave.text.toString()
      val leaveDate = dateOfLeave.text.toString()
      db.collection("Leave Applicants").document(userId).collection("Leave Application").document("Leave $leaveNo").set(ApplicationLeave(userId, employeeName, dateLeave, typeOfLeave, numOfLeave, leaveDate))
         .addOnSuccessListener {
            Toast.makeText(this, "Leave Application Successfully", Toast.LENGTH_LONG).show()
            val intent = Intent(this, LeaveMonitor::class.java)
            intent.putExtra("status", "Pending")
            intent.putExtra("username", name)
            intent.putExtra("id", userId)
            startActivity(intent)
         }
         .addOnFailureListener {
            Toast.makeText(this, "Leave Application Failed", Toast.LENGTH_LONG).show()
         }
   }
}