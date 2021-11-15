package com.example.aturaja

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FriendsAdapter (private val friendList : ArrayList<Friends>) : RecyclerView.Adapter<FriendsAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.friends_layout, parent,
            false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = friendList[position]

        holder.friendImage.setImageResource(currentItem.friendImage)
        holder.friendName.text = currentItem.friendName
        holder.friendEmail.text = currentItem.friendEmail
    }

    override fun getItemCount(): Int {

        return friendList.size
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        var friendImage : ImageView = itemView.findViewById(R.id.imageViewFriends)
        var friendName : TextView = itemView.findViewById(R.id.textViewFriendName)
        var friendEmail : TextView = itemView.findViewById(R.id.textViewFriendEmail)

    }
}