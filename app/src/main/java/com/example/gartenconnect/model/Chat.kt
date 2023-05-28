package com.example.gartenconnect.model

import com.google.firebase.auth.FirebaseAuth

data class Chat(
    var id: String? = null,
    var senderId: String? = null,
    var receiverId: String? = null,
    var message: String? = null,
    var timestamp: Long? = null
){
    fun isSendByMe(): Boolean = senderId.equals(FirebaseAuth.getInstance().currentUser?.uid)
}

