package com.example.gartenconnect.model

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class RecentUpdateRepository {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val updatesCollection: CollectionReference = firestore.collection("updates")

    fun addUpdate(update: Update) {
        updatesCollection.add(update)
            .addOnSuccessListener {
                Log.i("RecentUpdateRepository", "Actualización guardada correctamente")
            }
            .addOnFailureListener { exception ->
                Log.e(
                    "RecentUpdateRepository",
                    "Error al guardar la actualización: ${exception.message}"
                )
            }
    }

    fun getRecentUpdates(limit: Int, onComplete: (List<Update>) -> Unit) {
        updatesCollection
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(limit.toLong())
            .get()
            .addOnSuccessListener { querySnapshot ->
                val updates = mutableListOf<Update>()
                for (document in querySnapshot.documents) {
                    val update = document.toObject(Update::class.java)
                    if (update != null) {
                        updates.add(update)
                    }
                }
                onComplete(updates)
            }
            .addOnFailureListener { exception ->
                Log.e(
                    "RecentUpdateRepository",
                    "Error al obtener las actualizaciones: ${exception.message}"
                )
            }
    }
}
