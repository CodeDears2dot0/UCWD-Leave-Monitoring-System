package com.example.softengdevtest

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.softengdevtest.databinding.ActivitySignUpBinding
import com.google.android.material.textfield.TextInputLayout
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
   private lateinit var textInputForPassword : TextInputLayout
   private lateinit var progressBar: ProgressBar
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      enableEdgeToEdge()
      binding = ActivitySignUpBinding.inflate(layoutInflater)
      setContentView(binding.root)
      progressBar = findViewById(R.id.progressBar)
      textInputForPassword = findViewById(R.id.textInput_forPassword)
      editTextEmpName = findViewById(R.id.editTextEmpName)
      editTextEmpID = findViewById(R.id.editTextempID)
      editTextEmail = findViewById(R.id.editTextemail)
      editTextPassword = findViewById(R.id.editTextPassword)
      editTextRePassword = findViewById(R.id.editTextrePassword)
      createBtn = findViewById(R.id.createbtn)

      editTextPassword.addTextChangedListener(object : TextWatcher {
         override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            //Do Something
         }
         override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            //Do Something
         }
         override fun afterTextChanged(s: Editable?) {
            val password = s.toString()
            val strength = calculatePasswordStrength(password)
            textInputForPassword.helperText = strength
            when (strength) {
                "Weak" -> {
                   progressBar.setProgress(25, true)
                }
                "Medium" -> {
                   progressBar.setProgress(75, true)
                }
                "Strong" -> {
                   progressBar.setProgress(100, true)
                }
            }
         }
      })
      dbRef = FirebaseDatabase.getInstance().getReference("Employees Accounts")
      createBtn.setOnClickListener {
         if (editTextEmpName.text.toString().isEmpty() || editTextEmpID.text.toString().isEmpty() || editTextEmail.text.toString().isEmpty() || editTextPassword.text.toString().isEmpty()){
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_LONG).show()
            editTextPassword.error = "Please fill in all fields"
            editTextRePassword.error = "Please fill in all fields"
            editTextEmpName.error = "Please fill in all fields"
            editTextEmpID.error = "Please fill in all fields"
            editTextEmail.error = "Please fill in all fields"
         }else{
            saveEmployeeData()
         }
      }
   }
   private fun saveEmployeeData(){
      if (editTextPassword.text.toString() != editTextRePassword.text.toString()) {
         editTextPassword.error = "Password does not match"
         editTextRePassword.error = "Password does not match"
      }else {
         if (!checkIfExists()){
            val fullName = editTextEmpName.text.toString()
            val employeeID = editTextEmpID.text.toString()
            val employeeEmail = editTextEmail.text.toString()
            val employeePassword = editTextPassword.text.toString()


            db.collection("Employees Accounts").document(employeeID)
               .set(EmployeeData(fullName, employeeID, employeeEmail, employeePassword))
               .addOnSuccessListener {
                  Toast.makeText(this, "Employee Account Created Successfully", Toast.LENGTH_LONG)
                     .show()
                  startActivity(Intent(this, LoginActivity::class.java))
               }
               .addOnFailureListener {
                  Toast.makeText(this, "Employee Account Creation Failed", Toast.LENGTH_LONG).show()
               }
         }
         else{
            Toast.makeText(this, "Employee Account Already Exists", Toast.LENGTH_LONG).show()
         }
      }
   }
   private fun calculatePasswordStrength(password: String): String {
      progressBar.visibility = View.VISIBLE
      return when {
         password.length < 8 -> "Weak"
         password.length >= 8 && containsSpecialCharacter(password) && !containsUpperCase(password) -> "Medium"
         password.length >= 8 && containsSpecialCharacter(password) && containsUpperCase(password) -> "Strong"
         else -> "Weak"
      }
   }
   private fun containsSpecialCharacter(password: String): Boolean {
      val specialCharacters = "!@#$%^&*"
      return password.any { it in specialCharacters }
   }
   private fun containsUpperCase(password: String): Boolean {
      for (letter in password) {
         if (letter.isUpperCase()) {
            return true
         }
      }
      return false
   }
   private fun checkIfExists(): Boolean{
      val ref = db.collection("Employees Accounts").document(editTextEmpID.text.toString())
      return ref.get().isSuccessful
   }
}