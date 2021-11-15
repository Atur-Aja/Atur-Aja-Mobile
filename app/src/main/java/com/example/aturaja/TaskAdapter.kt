package com.example.aturaja

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter (private val taskList : ArrayList<Task>) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.task_layout, parent,
            false)

        return ViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = taskList[position]

        holder.taskName.text = currentItem.taskName
        holder.taskHour.text = currentItem.taskHour
        holder.taskDate.text = currentItem.taskDate


    }

    override fun getItemCount(): Int {

        return taskList.size

    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        var taskName : TextView = itemView.findViewById(R.id.textViewTask)
        var taskHour : TextView = itemView.findViewById(R.id.textViewTime)
        var taskDate : TextView = itemView.findViewById(R.id.textViewDate)

    }

}
