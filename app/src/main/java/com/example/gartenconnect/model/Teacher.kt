package com.example.gartenconnect.model

data class Teacher(
    var id: String? = null,
    val name: String? = null,
    val photoUrl: String? = null,
    val classIds: List<String>? = null
)
