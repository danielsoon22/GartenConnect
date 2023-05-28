package com.example.gartenconnect.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gartenconnect.R
import com.example.gartenconnect.model.StudentAttendance
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AtterndanceStudentAdapter(var attendanceList: List<StudentAttendance>) :
    RecyclerView.Adapter<AtterndanceStudentAdapter.UpdateViewHolder>() {
    inner class UpdateViewHolder(itemView: ViewGroup) : RecyclerView.ViewHolder(itemView) {
        val date: TextView = itemView.findViewById(R.id.dateTextView)
        val hasAttended: CheckBox = itemView.findViewById(R.id.attendanceCheckBox)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AtterndanceStudentAdapter.UpdateViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.attendance_parent_item, parent, false)
        return UpdateViewHolder(view as ViewGroup)
    }

    override fun onBindViewHolder(
        holder: AtterndanceStudentAdapter.UpdateViewHolder,
        position: Int
    ) {
        val attendance = attendanceList[position]
        val timestamp = attendance.date
        val milliseconds = timestamp!!.seconds * 1000 + timestamp.nanoseconds / 1000000
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        holder.date.text = sdf.format(Date(milliseconds))
        holder.hasAttended.isChecked = attendance.hasAttended == true
    }

    override fun getItemCount(): Int = attendanceList.size

    fun updateData(newData: List<StudentAttendance>) {
        // Reemplaza todos los datos
        this.attendanceList = newData
        // Notifica al adaptador que los datos han cambiado
        notifyDataSetChanged()
    }


}
