package com.example.softengdevtest

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class LeaveApplicationActivity : AppCompatActivity() {
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContentView(R.layout.activity_leave_application01)

      findViewById<Button>(R.id.savebtn).setOnClickListener {
         val intent = Intent(this, LeaveMonitor ::class.java)

         val builder = AlertDialog.Builder(this)
            .setTitle("Leave Application")
            .setMessage("Are you sure you want to submit the leave application?")
            .setPositiveButton("Yes"){_, _->
               startActivity(intent)
            }
            .setNegativeButton("No"){dialog, _->
               dialog.dismiss()
            }
         builder.create().show()

      }

   }
}