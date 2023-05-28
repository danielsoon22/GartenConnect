package com.example.gartenconnect.model

data class Student(
    val id: String? = null,
    val name: String? = null,
    val classId: String? = null,
    val age: Int? = null,
    val photoUrl: String? = null,
    val progress: List<String>? = null,
    val attendance: List<String>? = null
)

