package com.example.gartenconnect.adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.gartenconnect.R
import com.example.gartenconnect.ViewModel.TeacherViewModel
import com.example.gartenconnect.databinding.MessageItemBinding
import com.example.gartenconnect.model.Chat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatAdapter(
    var chatList: List<Chat>,
    private val fragmentManager: FragmentManager,
    private val lifecycleOwner: LifecycleOwner,
    private val teacherViewModel: TeacherViewModel
) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {
inner class ChatViewHolder(itemView: ViewGroup) : RecyclerView.ViewHolder(itemView) {
    val binding = MessageItemBinding.bind(itemView)
    val chatMessage: TextView = itemView.findViewById(R.id.message_text_view)
    val chatTime: TextView = itemView.findViewById(R.id.time_text_view)
}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
        return ChatViewHolder(view as ViewGroup)
    }

    override fun getItemCount(): Int = chatList.size

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = chatList[position]
        val timestamp = chat.timestamp
        val milliseconds = timestamp!! * 1000
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        holder.binding.root.gravity = Gravity.START
        holder.chatMessage.text = chat.message
        holder.chatTime.text = sdf.format(Date(milliseconds))
    }

    fun updateData(newData: List<Chat>) {
        // Reemplaza todos los datos
        this.chatList = newData
        // Notifica al adaptador que los datos han cambiado
        notifyDataSetChanged()
    }

}
