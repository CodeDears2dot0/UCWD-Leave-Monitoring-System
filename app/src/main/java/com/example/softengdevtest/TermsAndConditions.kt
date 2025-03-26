package com.example.softengdevtest

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import androidx.fragment.app.Fragment

class TermsAndConditions : Fragment() {
    private lateinit var checkBox : CheckBox
    private lateinit var save : Button
    private lateinit var cancel : Button
    private var empName: String? = null
    private var empID: String? = null
    private var empEmail: String? = null
    private var empPassword: String? = null
    private var isCancelled: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            empName = it.getString("empName")
            empID = it.getString("empID")
            empEmail = it.getString("empEmail")
            empPassword = it.getString("empPassword")
            isCancelled = it.getString("isCancelled")
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_terms_and_conditions, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        save = view.findViewById(R.id.save)!!
        cancel = view.findViewById(R.id.cancel)!!
        checkBox = view.findViewById(R.id.checkbox)!!
        save.visibility = View.INVISIBLE
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                save.visibility = View.VISIBLE
                save.setOnClickListener {
                    isCancelled = "true"
                    val intent = Intent(requireContext(), SignUpActivity::class.java)
                    intent.putExtra("empName", empName)
                    intent.putExtra("empID", empID)
                    intent.putExtra("empEmail", empEmail)
                    intent.putExtra("empPassword", empPassword)
                    intent.putExtra("isCancelled", isCancelled)
                    startActivity(intent)
                }
            }
        }
        cancel.setOnClickListener {
            isCancelled = "false"
            val intent = Intent(requireContext(), SignUpActivity::class.java)
            intent.putExtra("empName", empName)
            intent.putExtra("empID", empID)
            intent.putExtra("empEmail", empEmail)
            intent.putExtra("empPassword", empPassword)
            intent.putExtra("isCancelled", isCancelled)
            startActivity(intent)
        }
    }
}