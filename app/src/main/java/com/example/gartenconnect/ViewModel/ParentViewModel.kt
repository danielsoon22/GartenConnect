package com.example.gartenconnect.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gartenconnect.model.Parent
import com.example.gartenconnect.model.ParentRepository
import com.google.firebase.auth.FirebaseAuth

class ParentViewModel : ViewModel() {
    private val TAG = "Log:ParentVM"
    private val repository: ParentRepository = ParentRepository()
    val parentLiveData: MutableLiveData<Parent?> = MutableLiveData()
    private val uid = FirebaseAuth.getInstance().currentUser?.uid

    fun getParent() {
        Log.i(TAG, "getParent: $uid")
        if (uid != null) {
            repository.getParents(uid).addOnSuccessListener { documentSnapshot ->
                val parent = documentSnapshot.toObject(Parent::class.java)
                Log.i(TAG, "getParent: $parent")
                parentLiveData.value = parent
            }
        }
    }

}