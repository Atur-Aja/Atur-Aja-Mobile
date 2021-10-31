package com.example.aturaja.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aturaja.R
import com.example.aturaja.activity.EditDeleteSchedule
import com.example.aturaja.model.CreateScheduleResponse
import com.example.aturaja.model.GetScheduleResponse
import com.example.aturaja.model.Schedule
import java.text.SimpleDateFormat

class ScheduleAdapter (private val scheduleList : ArrayList<GetScheduleResponse>) : RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {
    private val timeFormatDB = SimpleDateFormat("HH:mm:ss")
    private val timeFormatView = SimpleDateFormat("hh:mm a")

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var startHour : TextView = itemView.findViewById(R.id.tvStartHour)
        var endHour : TextView = itemView.findViewById(R.id.tvEndHour)
        var schedule : TextView = itemView.findViewById(R.id.tvSchedule)

        fun bind(getResponse: GetScheduleResponse) {
            with(itemView) {
                schedule.setText(getResponse.title)
                startHour.setText(timeFormatView.format(timeFormatDB.parse(getResponse.startTime)))
                endHour.setText(timeFormatView.format(timeFormatDB.parse(getResponse.endTime)))
                itemView.setOnClickListener{
                    var intent = Intent(itemView.context, EditDeleteSchedule::class.java)
                    intent.putExtra("id", getResponse.userId)
                    intent.putExtra("title", getResponse.title)
                    intent.putExtra("startDate", getResponse.startDate)
                    intent.putExtra("startTime", timeFormatView.format(timeFormatDB.parse(getResponse.startTime)))
                    intent.putExtra("endDate", getResponse.endDate)
                    intent.putExtra("endTime", timeFormatView.format(timeFormatDB.parse(getResponse.endTime)))

                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.schedule_layout, parent,
            false)

        return ViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(scheduleList[position])
    }

    override fun getItemCount(): Int {
        return scheduleList.size
    }
}