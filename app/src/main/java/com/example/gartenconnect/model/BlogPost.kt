package com.example.gartenconnect.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class BlogPost(
    val id: String? = null,
    val title: String? = null,
    val content: String? = null,
    val authorId: String? = null,
    val photoUrl: String? = null,
    val date: Timestamp? = null
): Parcelable