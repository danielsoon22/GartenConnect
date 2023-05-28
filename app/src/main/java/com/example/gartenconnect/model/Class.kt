package com.example.gartenconnect.model

data class Class(
    val id: String? = null,
    val name: String? = null,
    val studentIds: List<String>? = null,
    val teacherId: String? = null
)