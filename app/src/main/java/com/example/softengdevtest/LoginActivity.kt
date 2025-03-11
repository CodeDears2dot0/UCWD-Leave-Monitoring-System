package com.example.softengdevtest


import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.softengdevtest.databinding.ActivityLoginBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity() {

   private lateinit var binding: ActivityLoginBinding
   private lateinit var firebaseDatabase: FirebaseDatabase
   private lateinit var databaseReference: DatabaseReference
   private lateinit var editTextID : EditText
   private lateinit var editTextPassword : EditText

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      enableEdgeToEdge()
      binding = ActivityLoginBinding.inflate(layoutInflater)
      setContentView(binding.root)

      firebaseDatabase = FirebaseDatabase.getInstance()
      databaseReference = firebaseDatabase.getReference("Employees Accounts")
      editTextID = findViewById(R.id.editTextID)
      editTextPassword = findViewById(R.id.editTextPassword)

      binding.loginbtn.setOnClickListener {
         val id = editTextID.text.toString()
         val pass = editTextPassword.text.toString()
         if (id.isEmpty() && pass.isEmpty()){
            editTextID.error = "Please Enter Employee ID"
            editTextPassword.error = "Please Enter Password!!"
         }else if (id.isEmpty())editTextID.error = "Please Enter Employee ID"
         else if (pass.isEmpty())editTextPassword.error = "Please Enter Password!!"
         else signInUser()
      }
      binding.signUp.setOnClickListener{
         var intent = Intent(this, SignUpActivity::class.java)
         startActivity(intent)
      }
   }
   private fun signInUser(){
      val intent = Intent(this, LeaveMonitor::class.java)
      val employeeId = editTextID.text.toString()
      val employeePassword = editTextPassword.text.toString()
      databaseReference.child(employeeId).get().addOnSuccessListener {
         if (it.exists()){
            val password = it.child("employeePassword").value
            val username = it.child("fullName").value.toString()
            val id = it.child("employeeID").value.toString()
            if (password == employeePassword){
               intent.putExtra("username", username)
               intent.putExtra("id", id)
               startActivity(intent)
               Toast.makeText(this, "Login Successfully!!", Toast.LENGTH_LONG).show()
               finish()
            }else{
               Toast.makeText(this, "Invalid Password", Toast.LENGTH_LONG).show()
            }
         }
      }
   }
}