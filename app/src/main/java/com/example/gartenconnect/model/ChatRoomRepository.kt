package com.example.gartenconnect.model

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatRoomRepository {
    private val db = FirebaseDatabase.getInstance()

    fun getChatRooms(userId: String, onSuccess: (DataSnapshot) -> Unit, onFailure: (DatabaseError) -> Unit) {
        db.reference.child("chatRooms").orderByChild("user1Id").equalTo(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    Log.i("ChatRoomRepository", "Salas de chat obtenidas correctamente ${dataSnapshot.childrenCount}")
                    onSuccess(dataSnapshot)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    onFailure(databaseError)
                }
            })
    }

    fun saveChatRoom(chatRoom: ChatRoom, onSuccess: () -> Unit, onFailure: (DatabaseError) -> Unit) {
        val chatRoomId = db.reference.child("chatRooms").push().key
        if (chatRoomId != null) {
            chatRoom.id = chatRoomId
        }

        db.reference.child("chatRooms").child(chatRoomId!!).setValue(chatRoom)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onFailure(DatabaseError.fromException(e))
            }
    }
}