package com.example.gartenconnect.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gartenconnect.model.Chat
import com.example.gartenconnect.model.ChatRepository
import com.google.firebase.database.DatabaseError

class ChatViewModel: ViewModel() {
    private val chatRepository: ChatRepository = ChatRepository()
    private val _chat = MutableLiveData<List<Chat>>()
    val chatLiveData: LiveData<List<Chat>> = _chat
    private val _errorLiveData = MutableLiveData<DatabaseError>()
    val errorLiveData: LiveData<DatabaseError> = _errorLiveData

    fun createMessage(chatRoomId: String, senderId: String, receiverId: String, message: String) {
        chatRepository.createMessage(chatRoomId, senderId, receiverId, message,
            onSuccess = {},
            onFailure = { error ->
                _errorLiveData.value = error
            }
        )
    }
    fun getChats(chatRoomId: String) {
        chatRepository.getChats(chatRoomId,
            onSuccess = { chats ->
                _chat.value = chats
            },
            onFailure = { error ->
                _errorLiveData.value = error
            }
        )
    }
}