package com.example.gartenconnect.model

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class BlogRepository {

    private val db = FirebaseFirestore.getInstance()

    fun getBlogs(): Task<QuerySnapshot> {
        return db.collection("blogs").get()
    }
}