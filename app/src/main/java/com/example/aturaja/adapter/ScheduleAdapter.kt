package com.example.aturaja.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aturaja.R
import com.example.aturaja.model.Schedule

class ScheduleAdapter (private val scheduleList : ArrayList<Schedule>) : RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.schedule_layout, parent,
            false)

        return ViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = scheduleList[position]

        holder.schedule.text = currentItem.schedule
        holder.endHour.text = currentItem.endHour.toString()
        holder.startHour.text = currentItem.startHour.toString()

    }

    override fun getItemCount(): Int {

        return scheduleList.size

    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        var startHour : TextView = itemView.findViewById(R.id.tvStartHour)
        var endHour : TextView = itemView.findViewById(R.id.tvEndHour)
        var schedule : TextView = itemView.findViewById(R.id.tvSchedule)

    }

}