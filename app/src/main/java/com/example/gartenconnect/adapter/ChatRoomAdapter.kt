package com.example.gartenconnect.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.gartenconnect.R
import com.example.gartenconnect.ViewModel.TeacherViewModel
import com.example.gartenconnect.fragment.ChatRoomFragmentDirections
import com.example.gartenconnect.model.ChatRoom

class ChatRoomAdapter(
    var chatRoomList: List<ChatRoom>,
    private val lifecycleOwner: LifecycleOwner,
    private val teacherViewModel: TeacherViewModel,
) : RecyclerView.Adapter<ChatRoomAdapter.ChatRoomViewHolder>() {
    inner class ChatRoomViewHolder(itemView: ViewGroup) : RecyclerView.ViewHolder(itemView) {
        val chatRoomName: TextView = itemView.findViewById(R.id.chat_user_name)
        val chatLastMessage: TextView = itemView.findViewById(R.id.chat_last_message)
        val chatRoomImage: ImageView = itemView.findViewById(R.id.chat_profile_image)

        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val chatRoom = chatRoomList[position]

                    val action =
                        ChatRoomFragmentDirections.actionChatFragmentToMessageFragment(chatRoom)
                    it.findNavController().navigate(action)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_item, parent, false)
        return ChatRoomViewHolder(view as ViewGroup)
    }

    override fun getItemCount(): Int = chatRoomList.size

    override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) {
        val chatRoom = chatRoomList[position]
        val user2Id = chatRoom.user2Id!!

        teacherViewModel.getTeacherById(user2Id).observe(lifecycleOwner) { teacher ->
            Log.i("ChatRoomAdapter", "Teacher: $teacher")
            holder.chatRoomName.text = teacher.name
            Glide.with(holder.itemView.context).load(teacher.photoUrl).apply(
                RequestOptions.bitmapTransform(
                    CircleCrop()
                )
            ).into(holder.chatRoomImage)
        }

        holder.chatLastMessage.text = chatRoom.chats?.lastOrNull() ?: ""
    }

    fun updateData(newData: List<ChatRoom>) {
        chatRoomList = newData
        notifyDataSetChanged()
    }
}
