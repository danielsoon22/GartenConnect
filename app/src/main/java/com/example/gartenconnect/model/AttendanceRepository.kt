package com.example.gartenconnect.model

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class AttendanceRepository {
    private val db = FirebaseFirestore.getInstance()

    fun getAttendanceOfChild(attendanceId: String): Task<DocumentSnapshot> {
        return db.collection("attendance").document(attendanceId).get()
    }

}
