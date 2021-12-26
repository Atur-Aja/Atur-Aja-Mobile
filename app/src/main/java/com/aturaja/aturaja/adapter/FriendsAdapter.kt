package com.aturaja.aturaja.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aturaja.aturaja.R
import com.aturaja.aturaja.model.GetFriendResponse

class FriendsAdapter (private val friendList : ArrayList<GetFriendResponse>) : RecyclerView.Adapter<FriendsAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.friends_layout, parent,
            false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = friendList[position]

        holder.friendImage.setImageResource(R.drawable.a)
        holder.friendName.text = currentItem.username
        holder.friendEmail.text = currentItem.email
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