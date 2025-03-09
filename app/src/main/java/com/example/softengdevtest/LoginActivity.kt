package com.example.softengdevtest


import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.softengdevtest.databinding.ActivityLoginBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

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
         startActivity(Intent(this@LoginActivity, LeaveMonitor::class.java))
         finish()
      }
      binding.signUp.setOnClickListener{
         var intent = Intent(this, SignUpActivity::class.java)
         startActivity(intent)
      }
   }
   private fun signupUser(userID: String, password: String){
      databaseReference.orderByChild("employeeID").equalTo(userID).addListenerForSingleValueEvent(object : ValueEventListener{
         override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists()){
               for (userSnapshot in dataSnapshot.children){
                  val employeeData = userSnapshot.getValue(EmployeeData::class.java)

                  if (employeeData != null && employeeData.employeePassword == password){
                     Toast.makeText(this@LoginActivity, "Login Successful", Toast.LENGTH_SHORT).show()
                     return
                  }
               }
            }
            Toast.makeText(this@LoginActivity, "Invalid Username or Password", Toast.LENGTH_SHORT).show()
         }

         override fun onCancelled(error: DatabaseError) {
            Toast.makeText(this@LoginActivity, "Database Error: ${error.message}", Toast.LENGTH_SHORT).show()
         }
      })
   }
}