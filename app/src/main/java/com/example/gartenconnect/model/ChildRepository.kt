package com.example.gartenconnect.model


import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class ChildRepository {
    private val db = FirebaseFirestore.getInstance()

    fun getChildrenOfParent(parentId: String): Task<List<DocumentSnapshot>> {
        val taskCompletionSource = TaskCompletionSource<List<DocumentSnapshot>>()
        db.collection("parents").document(parentId).get()
            .addOnSuccessListener { parentDocument ->
                val childrenIds = parentDocument?.get("children") as? List<String> ?: emptyList()
                val childrenDocumentsTasks = childrenIds.map { childId ->
                    db.collection("students").document(childId).get()
                }
                Tasks.whenAllSuccess<DocumentSnapshot>(childrenDocumentsTasks)
                    .addOnSuccessListener { childrenDocuments ->
                        taskCompletionSource.setResult(childrenDocuments)
                    }
                    .addOnFailureListener { exception ->
                        taskCompletionSource.setException(exception)
                    }
            }
            .addOnFailureListener { exception ->
                taskCompletionSource.setException(exception)
            }
        return taskCompletionSource.task
    }
    fun getAttendesIds(uid: String): List<String>{
        return db.collection("students").document(uid).get().addOnSuccessListener { documentSnapshot ->
            documentSnapshot?.get("attendance") as? List<String> ?: emptyList()
        }.result as List<String>

    }
}