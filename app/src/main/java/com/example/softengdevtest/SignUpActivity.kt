package com.example.softengdevtest

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.softengdevtest.databinding.ActivitySignUpBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {
   private lateinit var dbRef: DatabaseReference
   private lateinit var editTextEmpName: EditText
   private lateinit var editTextEmpID: EditText
   private lateinit var editTextEmail: EditText
   private lateinit var editTextPassword: EditText
   private lateinit var editTextRePassword: EditText
   private lateinit var createBtn: Button
   private lateinit var binding : ActivitySignUpBinding
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      enableEdgeToEdge()
      binding = ActivitySignUpBinding.inflate(layoutInflater)
      setContentView(binding.root)

      editTextEmpName = findViewById(R.id.editTextEmpName)
      editTextEmpID = findViewById(R.id.editTextempID)
      editTextEmail = findViewById(R.id.editTextemail)
      editTextPassword = findViewById(R.id.editTextPassword)
      editTextRePassword = findViewById(R.id.editTextrePassword)
      createBtn = findViewById(R.id.createbtn)

      dbRef = FirebaseDatabase.getInstance().getReference("Employees Accounts")
      createBtn.setOnClickListener {
         saveEmployeeAccount()
      }
   }

   private fun saveEmployeeAccount(){
      val fullEmployeeName = editTextEmpName.text.toString()
      val employeeID = editTextEmpID.text.toString()
      val employeeEmail = editTextEmail.text.toString()
      val employeePassword = editTextPassword.text.toString()
      val employeeRePassword = editTextRePassword.text.toString()

      val signUpID = dbRef.push().key!!

      val employeeData = EmployeeData(fullEmployeeName, employeeID, employeeEmail, employeePassword)
      if (fullEmployeeName.isEmpty() && employeeID.isEmpty() && employeeEmail.isEmpty() && employeePassword.isEmpty()) {
         editTextEmpName.error = "Please enter your full name"
         editTextEmpID.error = "Please enter your employee ID"
         editTextEmail.error = "Please enter your email"
         editTextPassword.error = "Please enter your password"
      }
      else if (fullEmployeeName.isEmpty()) {
         editTextEmpName.error = "Please enter your full name"
      }
      else if (employeeID.isEmpty()) {
         editTextEmpID.error = "Please enter your employee ID"
      }
      else if (employeeEmail.isEmpty()) {
         editTextEmail.error = "Please enter your email"
      }
      else if (employeePassword.isEmpty()) {
         editTextPassword.error = "Please enter your password"
      }
      else{
         if (employeePassword != employeeRePassword) {
            editTextRePassword.error = "Passwords do not match"
            return
         }else {
            dbRef.child(signUpID).setValue(employeeData)
               .addOnCompleteListener {
                  Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_LONG).show()
               }.addOnFailureListener { err ->
                  Toast.makeText(this, "Failed to create account ${err.message}", Toast.LENGTH_LONG)
                     .show()
               }
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
         }
      }
   }
}