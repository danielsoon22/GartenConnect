package com.example.gartenconnect.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatRoom(
    var id: String?=null,
    val user1Id: String?=null,
    val user2Id: String?=null,
    val chats: List<String>?=null
): Parcelable
