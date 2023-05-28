package com.example.gartenconnect.model

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class ParentRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val TAG = "Log:ParentRepo"

    fun getParents(uid: String): Task<DocumentSnapshot>{
        Log.i(TAG, "getParents: $uid")
        val prueba  = db.collection("parents").document(uid).get()
        Log.i(TAG, "getParents: $prueba")
        return  prueba
    }

    fun login(email: String, password: String): Task<AuthResult> {
        return auth.signInWithEmailAndPassword(email, password)
    }


}