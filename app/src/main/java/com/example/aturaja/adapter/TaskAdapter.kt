package com.example.aturaja.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aturaja.R
import com.example.aturaja.activity.EditDeleteSchedule
import com.example.aturaja.activity.EditDeleteTaskActivity
import com.example.aturaja.model.ScheduleElement
import com.example.aturaja.model.TaskElement
import com.example.aturaja.model.TasksItem
import java.text.SimpleDateFormat

class TaskAdapter (private val taskList : ArrayList<TasksItem>) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {
    private val dateFormatView = SimpleDateFormat("dd MMMM yyyy")
    private val dateFormatDB = SimpleDateFormat("yyyy-MM-dd")
    private val timeFormatDB = SimpleDateFormat("HH:mm:ss")
    private val timeFormatView = SimpleDateFormat("hh:mm a")
    private lateinit var onTaskClickCallback: OnTaskClickCallback

    interface OnTaskClickCallback {
        fun onTaskClicked(data: TasksItem)
    }

    fun setOnTaskClickCallback(onTaskClickCallback: OnTaskClickCallback) {
        this.onTaskClickCallback = onTaskClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.task_layout, parent,
            false)

        return ViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val currentItem = taskList[position]

        holder.taskName.text = currentItem.task?.title
        holder.taskDate.text = dateFormatView.format(dateFormatDB.parse(currentItem.task?.date))
        holder.taskHour.text = timeFormatView.format(timeFormatDB.parse(currentItem.task?.time))
        holder.itemView.setOnClickListener {
            onTaskClickCallback.onTaskClicked(taskList[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var taskName: TextView = itemView.findViewById(R.id.textViewTask)
        var taskHour: TextView = itemView.findViewById(R.id.textViewTime)
        var taskDate: TextView = itemView.findViewById(R.id.textViewDate)
    }
}
