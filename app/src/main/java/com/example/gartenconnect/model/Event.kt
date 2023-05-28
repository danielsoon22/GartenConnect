package com.example.gartenconnect.model

import java.util.Date

data class Event(
    val id: String,
    val title: String,
    val date: Date,
    val description: String
)
