package com.example.gartenconnect.model

data class Parent(
    val uid: String? = null,
    val name: String? = null,
    val email: String? = null,
    val children: List<String>? = null,
    val photoUrl: String? = null,
)
