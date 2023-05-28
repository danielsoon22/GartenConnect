package com.example.gartenconnect.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gartenconnect.model.Teacher
import com.example.gartenconnect.model.TeacherRepository
import kotlinx.coroutines.launch

class TeacherViewModel : ViewModel(){

    private val TAG = "Log:TeacherVM"
    private val repository: TeacherRepository = TeacherRepository()
    private val _teacher = MutableLiveData<List<Teacher>>()
    val teacherLiveData: LiveData<List<Teacher>> = _teacher

    init {
        loadTeachers()
    }

    fun getTeacherById(id: String): LiveData<Teacher> {
        val teacherData = MutableLiveData<Teacher>()
        repository.getTeacherById(id).addOnSuccessListener { document ->
            Log.i(TAG, "getTeacherById: $document")
            if (document != null && document.exists()) {
                val teacher = document.toObject(Teacher::class.java)
                teacher?.let {
                    teacherData.value = it
                }
            }
        }.addOnFailureListener { exception ->
            // Manejar el caso de error en caso de que la operación no tenga éxito
            Log.e("TeacherViewModel", "Error al obtener el profesor por ID: ${exception.message}")
        }
        return teacherData
    }


    private fun loadTeachers() {
        viewModelScope.launch {
            val teacherList = repository.getAllTeachers()
            _teacher.value = teacherList
        }
    }
}