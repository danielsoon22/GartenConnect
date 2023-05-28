package com.example.gartenconnect.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gartenconnect.model.ChildRepository
import com.example.gartenconnect.model.Student
import com.google.firebase.auth.FirebaseAuth

class ChildViewModel: ViewModel() {
    private val repository: ChildRepository = ChildRepository()
    private val _child = MutableLiveData<Student?>()
    val childLiveData: LiveData<Student?> = _child
    private val uid = FirebaseAuth.getInstance().currentUser?.uid

    fun loadChildrenOfParent() {
        repository.getChildrenOfParent(uid!!)
            .addOnSuccessListener { documents ->
                for (document in documents){
                   val child = document.toObject(Student::class.java)
                    Log.i("ChildViewModel", "Child: $child")
                    _child.value = child
                }
            }.addOnFailureListener {exception ->
                Log.e("ChildViewModel", "Error loading children", exception)
            }
    }
}