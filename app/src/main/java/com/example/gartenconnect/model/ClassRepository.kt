package com.example.gartenconnect.model

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class ClassRepository {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun getAllClasses(onSuccess: (List<Class>) -> Unit, onError: (Exception) -> Unit) {
        db.collection("classes")
            .get()
            .addOnSuccessListener { querySnapshot: QuerySnapshot? ->
                querySnapshot?.let {
                    val classes = mutableListOf<Class>()
                    for (document in querySnapshot.documents) {
                        val classData = document.toObject(Class::class.java)
                        classData?.let { classe ->
                            classes.add(classe)
                        }
                    }
                    onSuccess(classes)
                }
            }
            .addOnFailureListener { exception: Exception ->
                onError(exception)
            }
    }

    fun getClassById(classId: String, onSuccess: (Class?) -> Unit, onError: (Exception) -> Unit) {
        db.collection("classes")
            .document(classId)
            .get()
            .addOnSuccessListener { documentSnapshot: DocumentSnapshot? ->
                val classData = documentSnapshot?.toObject(Class::class.java)
                onSuccess(classData)
            }
            .addOnFailureListener { exception: Exception ->
                onError(exception)
            }
    }

    fun createClasse(classe: Class, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        db.collection("classes")
            .add(classe)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception: Exception ->
                onError(exception)
            }
    }
}