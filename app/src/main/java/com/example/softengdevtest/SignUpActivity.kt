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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
   private var db = Firebase.firestore
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
         if (editTextPassword.text.toString() != editTextRePassword.text.toString()) {
            editTextPassword.error = "Password does not match"
            editTextRePassword.error = "Password does not match"
         }else saveEmployeeData()
      }
   }
   private fun saveEmployeeData(){
      val fullName = editTextEmpName.text.toString()
      val employeeID = editTextEmpID.text.toString()
      val employeeEmail = editTextEmail.text.toString()
      val employeePassword = editTextPassword.text.toString()

      db.collection("Employees Accounts").document(employeeID).set(EmployeeData(fullName, employeeID, employeeEmail, employeePassword))
         .addOnSuccessListener {
            Toast.makeText(this, "Employee Account Created Successfully", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, LoginActivity::class.java))
         }
         .addOnFailureListener {
            Toast.makeText(this, "Employee Account Creation Failed", Toast.LENGTH_LONG).show()
         }
   }
}