package com.example.gartenconnect.model

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class TeacherRepository {
    private val db = FirebaseFirestore.getInstance()

    suspend fun getAllTeachers(): List<Teacher> {
        val teachers = mutableListOf<Teacher>()
        val querySnapshot = db.collection("teachers").get().await()
        for (document in querySnapshot.documents) {
            val teacher = document.toObject(Teacher::class.java)
            if (teacher != null) {
                teacher.id = document.id // Aquí asignas el ID del documento a tu objeto Teacher
                teachers.add(teacher)
            }
        }
        return teachers
    }


    fun getTeacherById(id: String): Task<DocumentSnapshot>{
        return db.collection("teachers").document(id).get()
    }
}