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


class LeaveApplicationActivity : AppCompatActivity() {

   private lateinit var dbRef: DatabaseReference
   private lateinit var applicantName : TextView
   private lateinit var typeLeave : Spinner
   private lateinit var dateLeave : TextView
   private lateinit var numLeave : EditText
   private lateinit var dateOfLeave : EditText
   private lateinit var userId : String
   private lateinit var name : String

   @SuppressLint("MissingInflatedId")
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContentView(R.layout.activity_leave_application01)

      //Extras
      name = intent.getStringExtra("username").toString()
      userId = intent.getStringExtra("id").toString()

      val dataToday = intent?.getStringExtra("dateToday")
      typeLeave = findViewById<Spinner>(R.id.spinner)

      ArrayAdapter.createFromResource(
         this,
         R.array.leaves,
         android.R.layout.simple_spinner_item
      ).also { adapter ->
         adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
         typeLeave.adapter = adapter
      }
      applicantName = findViewById<TextView>(R.id.employeeName)
      dateLeave = findViewById<TextView>(R.id.dateToday)
      dateLeave.text = dataToday
      numLeave = findViewById<EditText>(R.id.numLeave)
      dateOfLeave = findViewById<EditText>(R.id.leaveDate)
      applicantName.text = name
      dbRef = FirebaseDatabase.getInstance().getReference("Leave Applicants")



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


      val applicationID = userId
      val applicationRandomID = dbRef.push().key!!

      val applicationLeave = ApplicationLeave(applicationID, employeeName, typeOfLeave, dateLeave, numOfLeave, leaveDate)

      if (numOfLeave.isEmpty() && leaveDate.isEmpty()) {
         numLeave.error = "Please enter the number of leave"
         dateOfLeave.error = "Please enter the date of leave"
      }
      else if (numOfLeave.isEmpty()) {
         numLeave.error = "Please enter the number of leave"
      }
      else if (leaveDate.isEmpty()) {
         dateOfLeave.error = "Please enter the date of leave"
      }else{
         dbRef.child(applicationID).child(applicationRandomID).setValue(applicationLeave)
            .addOnCompleteListener {
               Toast.makeText(this, "Leave Application Submitted Successfully", Toast.LENGTH_LONG).show()
            }.addOnFailureListener { err ->
               Toast.makeText(this, "Failed to submit leave application ${err.message}", Toast.LENGTH_LONG).show()
            }
         val intent = Intent(this, LeaveMonitor::class.java)
         intent.putExtra("username", name)
         intent.putExtra("id", userId)
         startActivity(intent)
      }
   }
}