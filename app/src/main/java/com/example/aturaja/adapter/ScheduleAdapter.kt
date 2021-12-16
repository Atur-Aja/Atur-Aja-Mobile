package com.example.aturaja.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aturaja.R
import com.example.aturaja.model.SchedulesItem
import java.text.SimpleDateFormat

class ScheduleAdapter(private val scheduleList: ArrayList<SchedulesItem>) : RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {
    private val timeFormatDB = SimpleDateFormat("HH:mm:ss")
    private val timeFormatView = SimpleDateFormat("hh:mm a")
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onClickItem(data: SchedulesItem)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var startHour : TextView = itemView.findViewById(R.id.tvStartHour)
        var endHour : TextView = itemView.findViewById(R.id.tvEndHour)
        var schedule : TextView = itemView.findViewById(R.id.tvSchedule)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.schedule_layout, parent,
            false)

        return ViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = scheduleList[position]
        val timeStart = currentItem.schedule?.startTime
        val timeEnd = currentItem.schedule?.endTime

        holder.schedule.text = currentItem.schedule?.title
        holder.startHour.text = timeFormatView.format(timeFormatDB.parse(timeStart))
        holder.endHour.text = timeFormatView.format(timeFormatDB.parse(timeEnd))

        holder.itemView.setOnClickListener {
            onItemClickCallback.onClickItem(scheduleList[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int {
        return scheduleList.size
    }
}