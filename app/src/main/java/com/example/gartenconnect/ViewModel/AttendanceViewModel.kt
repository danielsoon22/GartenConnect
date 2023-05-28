package com.example.gartenconnect.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gartenconnect.model.AttendanceRepository
import com.example.gartenconnect.model.ChildRepository
import com.example.gartenconnect.model.Student
import com.example.gartenconnect.model.StudentAttendance
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot

class AttendanceViewModel : ViewModel() {

    private val attendanceRepository: AttendanceRepository = AttendanceRepository()
    private val childResporitory: ChildRepository = ChildRepository()
    private val _attendance = MutableLiveData<List<StudentAttendance?>>()
    val attendanceLiveData: LiveData<List<StudentAttendance?>> = _attendance
    private val uid = FirebaseAuth.getInstance().currentUser?.uid
    private val TAG = "Log:AttendanceVM"
    private val attendanceList = mutableListOf<StudentAttendance>()

    fun getAttendance(child: Student) {
        // Limpiar la lista antes de agregar nuevos datos
        if (attendanceList.isNotEmpty()) {attendanceList.clear()}
        Log.i(TAG, "getAttendance: $child")

        // Crear una lista para almacenar las promesas
        val tasks = mutableListOf<Task<DocumentSnapshot>>()

        child.attendance?.forEach { attendanceId ->
            Log.i(TAG, "getAttendance: $attendanceId")
            // Agregar la promesa a la lista
            tasks.add(attendanceRepository.getAttendanceOfChild(attendanceId))
        }

        // Esperar hasta que todas las promesas se hayan resuelto
        Tasks.whenAllSuccess<DocumentSnapshot>(tasks).addOnSuccessListener { documents ->
            documents.forEach { documentSnapshot ->
                val attendance = documentSnapshot.toObject(StudentAttendance::class.java)
                Log.i(TAG, "getAttendance1: $attendance")
                if (attendance != null) {
                    attendanceList.add(attendance)
                    Log.i(TAG, "getAttendance12: $attendanceList")
                }
            }
            // Actualizar el valor de LiveData aquí
            _attendance.value = attendanceList
            Log.i(TAG, "getAttendance3: $attendanceList")
        }
    }

    fun getAttendanceOfChild() {
        childResporitory.getChildrenOfParent(uid!!)
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val child = document.toObject(Student::class.java)
                    Log.i("ChildViewModel", "Child: $child")
                    getAttendance(child!!)
                }
            }.addOnFailureListener { exception ->
                Log.e("ChildViewModel", "Error loading children", exception)
            }
    }


}