package com.example.gartenconnect.model

import com.google.firebase.Timestamp

data class StudentAttendance(
    val AttenadnceId: String? = null,
    val date: Timestamp? = null,
    val hasAttended: Boolean? = null
)