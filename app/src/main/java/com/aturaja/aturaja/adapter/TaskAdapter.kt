package com.aturaja.aturaja.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aturaja.aturaja.R
import com.aturaja.aturaja.model.TasksItem
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
        val priority = currentItem.task?.priority

        holder.taskName.text = "${setPriority(priority)} ${currentItem.task?.title}"
        holder.taskDate.text = dateFormatView.format(dateFormatDB.parse(currentItem.task?.date))
        holder.taskHour.text = timeFormatView.format(timeFormatDB.parse(currentItem.task?.time))
        holder.itemView.setOnClickListener {
            onTaskClickCallback.onTaskClicked(taskList[holder.adapterPosition])
        }
    }

    private fun setPriority(priority: String?): String {
        var hasil = ""
        val priority = priority?.toInt()

        if(priority == 0) {
            hasil = ""
        } else if(priority == 1) {
            hasil = "!"
        } else if(priority == 2) {
            hasil = "!!"
        } else {
            hasil = "!!!"
        }

        return hasil
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