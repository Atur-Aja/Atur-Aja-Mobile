package com.example.aturaja

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GroupAdapter (private val groupList : ArrayList<Group>) : RecyclerView.Adapter<GroupAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.group_layout, parent,
            false)

        return ViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = groupList[position]

        holder.groupName.text = currentItem.groupName
        holder.groupHour.text = currentItem.groupHour
        holder.groupDate.text = currentItem.groupDate


    }

    override fun getItemCount(): Int {

        return groupList.size

    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        var groupName : TextView = itemView.findViewById(R.id.textViewTask)
        var groupHour : TextView = itemView.findViewById(R.id.textViewTime)
        var groupDate : TextView = itemView.findViewById(R.id.textViewDate)

    }

}