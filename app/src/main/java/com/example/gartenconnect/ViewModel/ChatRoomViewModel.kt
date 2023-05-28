package com.example.gartenconnect.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gartenconnect.model.ChatRoom
import com.example.gartenconnect.model.ChatRoomRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseError

class ChatRoomViewModel: ViewModel() {
    private val chatRoomRepository: ChatRoomRepository = ChatRoomRepository()
    private val _chatRoom = MutableLiveData<List<ChatRoom>>()
    val chatRoomLiveData: LiveData<List<ChatRoom>> = _chatRoom

    fun getChatRoom() {
        chatRoomRepository.getChatRooms(FirebaseAuth.getInstance().currentUser!!.uid,
            { dataSnapshot ->
                Log.i("ChatRoomViewModel", "Salas de chat obtenidas correctamente")
                val chatRooms = mutableListOf<ChatRoom>()
                for (snapshot in dataSnapshot.children) {
                    val chatRoomData = snapshot.getValue()
                    if (chatRoomData is HashMap<*, *>) {
                        val chatRoom = ChatRoom(
                            id = chatRoomData["id"] as? String,
                            user1Id = chatRoomData["user1Id"] as? String,
                            user2Id = chatRoomData["user2Id"] as? String,
                            chats = chatRoomData["chats"] as? List<String>
                        )
                        chatRooms.add(chatRoom)
                    }
                }
                _chatRoom.value = chatRooms
            },
            { error ->
                Log.e("ChatRoomViewModel", "Error al obtener las salas de chat: ${error.message}")
            }
        )

    }


    fun saveChatRoom(chatRoom: ChatRoom, onSuccess: () -> Unit, onFailure: (DatabaseError) -> Unit){
        chatRoomRepository.saveChatRoom(chatRoom, onSuccess, onFailure)
    }
}