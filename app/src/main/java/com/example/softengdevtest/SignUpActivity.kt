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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.softengdevtest.databinding.ActivitySignUpBinding
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.firestore
import com.google.firebase.Firebase

class SignUpActivity : AppCompatActivity() {
   private lateinit var fragmentManager: FragmentManager
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

      // Create and Initializing Intents and Fields
      val isCancelled = intent.getStringExtra("isCancelled")
      val empName = intent.getStringExtra("empName")
      val empID = intent.getStringExtra("empID")
      val empEmail = intent.getStringExtra("empEmail")
      val empPassword = intent.getStringExtra("empPassword")
      val empRePassword = intent.getStringExtra("empPassword")
      val nameField = findViewById<TextInputLayout>(R.id.textInput_forName)
      val idField = findViewById<TextInputLayout>(R.id.textInput_forID)
      val emailField = findViewById<TextInputLayout>(R.id.textInput_forEmail)
      val rePasswordField = findViewById<TextInputLayout>(R.id.textInput_forRePassword)

      // Initializing IDs
      progressBar = findViewById(R.id.progressBar)
      textInputForPassword = findViewById(R.id.textInput_forPassword)
      editTextEmpName = findViewById(R.id.editTextEmpName)
      editTextEmpID = findViewById(R.id.editTextempID)
      editTextEmail = findViewById(R.id.editTextemail)
      editTextPassword = findViewById(R.id.editTextPassword)
      editTextRePassword = findViewById(R.id.editTextrePassword)
      createBtn = findViewById(R.id.createbtn)

      // Setting Texts
      editTextEmpName.setText(empName)
      editTextEmpID.setText(empID)
      editTextEmail.setText(empEmail)
      editTextPassword.setText(empPassword)
      editTextRePassword.setText(empRePassword)

      // Visibility
      nameField.visibility = View.VISIBLE
      idField.visibility = View.VISIBLE
      emailField.visibility = View.VISIBLE
      rePasswordField.visibility = View.VISIBLE
      textInputForPassword.visibility = View.VISIBLE
      editTextPassword.visibility = View.VISIBLE
      editTextRePassword.visibility = View.VISIBLE
      progressBar.visibility = View.VISIBLE
      createBtn.visibility = View.VISIBLE
      editTextEmpName.visibility = View.VISIBLE
      editTextEmpID.visibility = View.VISIBLE
      editTextEmail.visibility = View.VISIBLE

      val builder = AlertDialog.Builder(this)
         .setTitle("Confirm Sign Up")
         .setMessage("Are you sure you want to sign up with your provided credentials?")
         .setPositiveButton("Yes") {_, _ ->
            val bundle = Bundle().apply {
               putString("empName", editTextEmpName.text.toString())
               putString("empID", editTextEmpID.text.toString())
               putString("empEmail", editTextEmail.text.toString())
               putString("empPassword", editTextPassword.text.toString())
               putString("isCancelled", "false")
            }
            editTextPassword.visibility = View.INVISIBLE
            editTextRePassword.visibility = View.INVISIBLE
            progressBar.visibility = View.INVISIBLE
            createBtn.visibility = View.INVISIBLE
            editTextEmpName.visibility = View.INVISIBLE
            editTextEmpID.visibility = View.INVISIBLE
            editTextEmail.visibility = View.INVISIBLE
            textInputForPassword.visibility = View.INVISIBLE
            nameField.visibility = View.INVISIBLE
            idField.visibility = View.INVISIBLE
            emailField.visibility = View.INVISIBLE
            rePasswordField.visibility = View.INVISIBLE
            goToFragment(TermsAndConditions(), bundle)
         }
         .setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
         }

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
            builder.create().show()
         }
      }
      if (isCancelled == "false" || isCancelled == null){
         Toast.makeText(this, "Sign Up", Toast.LENGTH_LONG).show()
      }else {
         checking { result ->
            if (result != null) {
               Toast.makeText(this, "Loading", Toast.LENGTH_LONG).show()
            } else {
               saveEmployeeData()
            }
         }
         saveEmployeeData()
      }
   }
   private fun saveEmployeeData(){
      if ((editTextPassword.text.toString() != editTextRePassword.text.toString()) || (editTextPassword.text.isEmpty() && editTextRePassword.text.isEmpty())) {
         Toast.makeText(this, "Fill all fields correctly", Toast.LENGTH_LONG).show()
      }else {
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

   // Savior Code
   private fun checking(callback: (String?) -> Unit) {
      val ref = db.collection("Employees Accounts").document(editTextEmpID.text.toString())
      ref.get().addOnSuccessListener { document ->
         val id = document.get("employeeID")
         callback(id?.toString()) // Call the callback with the result
      }.addOnFailureListener {
         callback(null) // Handle errors by sending null
      }
   }

   private fun goToFragment(fragment: Fragment, data: Bundle) {
      fragment.arguments = data
      fragmentManager = supportFragmentManager
      fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit()
   }
}