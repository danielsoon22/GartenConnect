package com.example.gartenconnect.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gartenconnect.model.Class
import com.example.gartenconnect.model.ClassRepository

class ClassViewModel: ViewModel() {

    private val _class = MutableLiveData<List<Class>>()
    private val classRepository: ClassRepository = ClassRepository()
    val classLiveData: LiveData<List<Class>> = _class

    fun getAllClasses(){
        classRepository.getAllClasses(
            onSuccess = { classes ->
                _class.value = classes
            },
            onError = { exception ->
                exception.message?.let { message ->
                    println(message)
                }
            }
        )
    }

    fun getClassById(classId: String){
        classRepository.getClassById(
            classId = classId,
            onSuccess = { classe ->
                classe?.let {
                    _class.value = listOf(it)
                }
            },
            onError = { exception ->
                exception.message?.let { message ->
                    println(message)
                }
            }
        )
    }

    fun createClasse(classe: Class){
        classRepository.createClasse(
            classe = classe,
            onSuccess = {
                println("Classe created")
            },
            onError = { exception ->
                exception.message?.let { message ->
                    println(message)
                }
            }
        )
    }
}