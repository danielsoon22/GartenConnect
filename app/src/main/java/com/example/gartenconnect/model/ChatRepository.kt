package com.example.gartenconnect.model

import com.google.firebase.Timestamp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatRepository {
    private val db = FirebaseDatabase.getInstance()

    fun createMessage(chatRoomId: String, senderId: String, receiverId: String, message: String, onSuccess: () -> Unit, onFailure: (DatabaseError) -> Unit) {
        val messageId = db.reference.child("chatRooms").child(chatRoomId).child("chats").push().key

        if (messageId != null) {
            val timestamp = Timestamp.now().seconds
            val newMessage = Chat(senderId = senderId, receiverId = receiverId, message = message, timestamp = timestamp)

            db.reference.child("chatRooms").child(chatRoomId).child("chats").child(messageId)
                .setValue(newMessage)
                .addOnSuccessListener {
                    onSuccess()
                }
                .addOnFailureListener { error ->
                    onFailure(DatabaseError.fromException(error))
                }
        } else {
            onFailure(DatabaseError.fromException(Exception("Failed to generate message ID")))
        }
    }

    fun getChats(chatRoomId: String, onSuccess: (List<Chat>) -> Unit, onFailure: (DatabaseError) -> Unit) {
        db.reference.child("chatRooms").child(chatRoomId).child("chats")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val chatsList = dataSnapshot.children.mapNotNull { chatSnapshot ->
                        chatSnapshot.getValue(Chat::class.java)
                    }
                    onSuccess(chatsList)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    onFailure(databaseError)
                }
            })
    }
}

