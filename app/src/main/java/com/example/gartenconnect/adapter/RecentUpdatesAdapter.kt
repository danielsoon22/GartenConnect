package com.example.gartenconnect.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gartenconnect.R
import com.example.gartenconnect.model.Update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RecentUpdatesAdapter(private var updates: List<Update>) :
    RecyclerView.Adapter<RecentUpdatesAdapter.UpdateViewHolder>() {
    inner class UpdateViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.recent_updates_title)
        val description: TextView = itemView.findViewById(R.id.recent_updates_content)
        val date: TextView = itemView.findViewById(R.id.recent_updates_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpdateViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recent_updates_card, parent, false)
        return UpdateViewHolder(view)
    }

    override fun getItemCount() = updates.size

    override fun onBindViewHolder(holder: UpdateViewHolder, position: Int) {
        val update = updates[position]
        val timestamp = update.timestamp
        val milliseconds = timestamp!!.seconds * 1000 + timestamp.nanoseconds / 1000000
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        holder.title.text = update.title
        holder.description.text = update.description
        holder.date.text = sdf.format(Date(milliseconds))
    }

    fun updateData(newData: List<Update>) {
        // Reemplaza todos los datos
         updates = newData
        // Notifica al adaptador que los datos han cambiado
        notifyDataSetChanged()
    }
}