package com.example.softengdevtest

data class ApplicationLeave(
    var applicationID: String,
    var applicant: String,
    var applicationDate: String,
    var applicationLeaveType: String,
    val applicationLeaveDays: String,
    val applicationLeaveDate: String,
    val applicationQuality: String,
    val applicationReason: String,
    val applicationStatus: String = "Pending"
)