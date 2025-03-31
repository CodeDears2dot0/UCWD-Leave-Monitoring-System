package com.example.softengdevtest

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Suppress("DEPRECATION")
class DateFragment : Fragment() {
    private lateinit var calendarView: CalendarView
    private lateinit var cancel : Button
    private lateinit var save : Button
    private var dateString: String? = null
    private var receivedId: String? = null
    private var receivedName : String? = null
    private var receivedDate : String? = null
    private var receivedTypePos : Int? = null
    private var receivedQualityPos : Int? = null
    private var receivedReason: String? = null
    private var receivedDays : String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            receivedDate = it.getString("dateToday")
            receivedName = it.getString("username")
            receivedId = it.getString("id")
            receivedTypePos = it.getInt("typeOfLeave")
            receivedQualityPos = it.getInt("quality")
            receivedReason = it.getString("reason")
            receivedDays = it.getString("days")
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_date, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calendarView = view.findViewById(R.id.calendarView2)
        cancel = view.findViewById(R.id.cancel)
        save = view.findViewById(R.id.save)

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // Create a Date object from the selected year, month, and day
            val calendar = Calendar.getInstance()

            // Set the year, month, and day (month is 0-based, so you don't need to adjust it)
            calendar.set(year, month, dayOfMonth)

            // Format the date to a string using SimpleDateFormat
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(calendar.time)

            // Display the formatted date in the TextView
             dateString = formattedDate
        }
        cancel.setOnClickListener {
            val intent = Intent(requireContext(), LeaveApplicationActivity::class.java)
            intent.putExtra("id", receivedId)
            intent.putExtra("dateToday", receivedDate)
            intent.putExtra("username", receivedName)
            intent.putExtra("dateWhenLeave", dateString)
            intent.putExtra("typeOfLeave", receivedTypePos)
            intent.putExtra("quality", receivedQualityPos)
            intent.putExtra("reason", receivedReason)
            intent.putExtra("days", receivedDays)
            startActivity(intent)
        }
        save.setOnClickListener {
            val intent = Intent(requireContext(), LeaveApplicationActivity::class.java)
            intent.putExtra("id", receivedId)
            intent.putExtra("dateToday", receivedDate)
            intent.putExtra("username", receivedName)
            intent.putExtra("dateWhenLeave", dateString)
            intent.putExtra("typeOfLeave", receivedTypePos)
            intent.putExtra("quality", receivedQualityPos)
            intent.putExtra("reason", receivedReason)
            intent.putExtra("days", receivedDays)
            startActivity(intent)
        }
    }
}