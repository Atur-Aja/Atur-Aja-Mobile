package com.aturaja.aturaja.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aturaja.aturaja.R
import com.aturaja.aturaja.model.SchedulesItem
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapterListSchedule (private val scheduleList: ArrayList<SchedulesItem>) : RecyclerView.Adapter<AdapterListSchedule.ViewHolder>(){
    private val timeFormatDB = SimpleDateFormat("HH:mm:ss", Locale.US)
    private val timeFormatView = SimpleDateFormat("hh:mm a", Locale.US)
    private val dateFormatDb = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val dateFormatView = SimpleDateFormat("dd MMMM yyyy", Locale.US)
    private lateinit var onItemClickCallback: OnItemClickCallbackListSchedule

    interface OnItemClickCallbackListSchedule {
        fun onClickItem(data: SchedulesItem)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallbackListSchedule) {
        this.onItemClickCallback = onItemClickCallback
    }


    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var startHour : TextView = itemView.findViewById(R.id.textViewTimeStart)
        var endHour : TextView = itemView.findViewById(R.id.textViewTimeEnd)
        var schedule : TextView = itemView.findViewById(R.id.textViewTask)
        var date: TextView = itemView.findViewById(R.id.textViewDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.schedule_list_layout, parent,
            false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = scheduleList[position]
        val timeStart = currentItem.schedule?.startTime
        val timeEnd = currentItem.schedule?.endTime
        val date = currentItem.schedule?.date

        holder.schedule.text = currentItem.schedule?.title
        holder.startHour.text = timeFormatView.format(timeFormatDB.parse(timeStart))
        holder.endHour.text = timeFormatView.format(timeFormatDB.parse(timeEnd))
        holder.date.text = dateFormatView.format(dateFormatDb.parse(date))

        holder.itemView.setOnClickListener {
            onItemClickCallback.onClickItem(scheduleList[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int {
        return scheduleList.size
    }

}