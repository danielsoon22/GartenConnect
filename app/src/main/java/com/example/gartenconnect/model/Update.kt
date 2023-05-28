package com.example.gartenconnect.model

import com.google.firebase.Timestamp

data class Update(
    val title: String? = null,
    val description: String? = null,
    val timestamp: Timestamp? = null
)