package com.example.softengdevtest

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase



class LeaveApplicationActivity : AppCompatActivity() {
   private lateinit var fragmentManager: FragmentManager
   private var db = Firebase.firestore
   private lateinit var dbRef: DatabaseReference
   private lateinit var applicantName : TextView
   private lateinit var typeLeave : Spinner
   private lateinit var qualityLeave : Spinner
   private lateinit var dateLeave : TextView
   private lateinit var numLeave : EditText
   private lateinit var dateOfLeave : TextView
   private lateinit var reasonOfLeave : EditText
   private lateinit var fragCon : FragmentContainerView
   private lateinit var cardView : CardView
   private lateinit var save : Button
   private lateinit var userId : String
   private lateinit var name : String
   private var dateWhenToLeave : String = "00/00/0000"
   private var typePos : Int = 0
   private var qualityPos : Int = 0
   private var applicationNum : Int = 0
   private var applicationNumNext : Int = 0
   @SuppressLint("MissingInflatedId", "SetTextI18n")
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContentView(R.layout.activity_leave_application01)
      //Extras

      name = intent.getStringExtra("username").toString()
      userId = intent.getStringExtra("id").toString()
      dateWhenToLeave = intent?.getStringExtra("dateWhenLeave").toString()
      typePos = intent?.getIntExtra("typeOfLeave", 0).toString().toInt()
      qualityPos = intent?.getIntExtra("quality", 0).toString().toInt()


      //Shared Preferences
      val sharedPref = getSharedPreferences("myPref", MODE_PRIVATE)
      val editor = sharedPref.edit()
      applicationNum = sharedPref.getInt("leaveNum", 0)
      if (applicationNum == applicationNumNext){
         applicationNumNext = applicationNum + 1
         editor.apply {
            putInt("leaveNum", applicationNumNext).apply()
         }
      }

      val dateToday = intent?.getStringExtra("dateToday")
      typeLeave = findViewById(R.id.spinner)
      qualityLeave = findViewById(R.id.spinner2)
      ArrayAdapter.createFromResource(
         this,
         R.array.leaves,
         android.R.layout.simple_spinner_item
      ).also { adapter ->
         adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
         typeLeave.adapter = adapter
      }
      typeLeave.setSelection(typePos)
      ArrayAdapter.createFromResource(
         this,
         R.array.quality,
         android.R.layout.simple_spinner_item
      ).also { adapter ->
         adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
         qualityLeave.adapter = adapter
      }
      qualityLeave.setSelection(qualityPos)
      fragCon = findViewById<FragmentContainerView>(R.id.fragmentContainer)
      applicantName = findViewById(R.id.employeeName)
      dateLeave = findViewById(R.id.dateToday)
      dateLeave.text = dateToday
      numLeave = findViewById(R.id.numLeave)
      dateOfLeave = findViewById(R.id.leaveDate)
      reasonOfLeave = findViewById(R.id.reason)
      cardView = findViewById(R.id.cardView2)
      save = findViewById(R.id.savebtn)
      dateOfLeave.text = dateWhenToLeave
      applicantName.text = name
      save.visibility = View.VISIBLE
      cardView.visibility = View.VISIBLE
      fragCon.visibility = View.INVISIBLE
      dbRef = FirebaseDatabase.getInstance().getReference("Leave Applicants")

      dateOfLeave.setOnClickListener {
         save.visibility = View.INVISIBLE
         cardView.visibility = View.INVISIBLE
         fragCon.visibility = View.VISIBLE
         val bundle = Bundle().apply{
            putString("id", userId)
            putString("username", name)
            putString("dateToday", dateToday)
            putInt("typeOfLeave", typeLeave.selectedItemPosition)
            putInt("quality", qualityLeave.selectedItemPosition)
         }
         val pos = qualityLeave.selectedItemPosition
         Toast.makeText(this, "Position $pos", Toast.LENGTH_LONG).show()
         goToFragment(DateFragment(), bundle)
      }

      save.setOnClickListener {
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
      val qualityOfLeave = qualityLeave.selectedItem.toString()
      val reason = reasonOfLeave.text.toString()
      db.collection("Leave Applicants").document(userId).collection("Leave Application").document("Leave $applicationNumNext").set(ApplicationLeave(userId, employeeName, dateLeave, typeOfLeave, numOfLeave, leaveDate, qualityOfLeave, reason))
         .addOnSuccessListener {
            Toast.makeText(this, "Leave Application Successfully", Toast.LENGTH_LONG).show()
            val intent = Intent(this, LeaveMonitor::class.java)
            intent.putExtra("username", name)
            intent.putExtra("id", userId)
            startActivity(intent)
         }
         .addOnFailureListener {
            Toast.makeText(this, "Leave Application Failed", Toast.LENGTH_LONG).show()
         }
   }
   private fun goToFragment(fragment: Fragment, data: Bundle) {
      fragment.arguments = data
      fragmentManager = supportFragmentManager
      fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit()
   }
}