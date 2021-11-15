package com.example.aturaja

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class MemberAdapter (private val memberList : ArrayList<Member>) : RecyclerView.Adapter<MemberAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.member_layout, parent,
            false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = memberList[position]

        holder.friendImage.setImageResource(currentItem.friendImage)
    }

    override fun getItemCount(): Int {

        return memberList.size

    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        var friendImage : ImageView = itemView.findViewById(R.id.friendImage)

    }
}