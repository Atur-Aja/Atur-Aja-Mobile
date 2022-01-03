package com.aturaja.aturaja.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aturaja.aturaja.R
import com.aturaja.aturaja.model.FriendsRecycler
import com.aturaja.aturaja.model.SchedulesItem

class FriendsAdapter(
    private val friendList: ArrayList<FriendsRecycler>
) : RecyclerView.Adapter<FriendsAdapter.ViewHolder>(){
    private lateinit var onFriendsFragmentClickCallback: OnFriendsFragmentClickCallback

    interface OnFriendsFragmentClickCallback {
        fun onClickItem(data: FriendsRecycler)
    }

    fun setOnFriendsFragmentClickCallback(onFriendsFragmentClickCallback: OnFriendsFragmentClickCallback) {
        this.onFriendsFragmentClickCallback = onFriendsFragmentClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.friends_layout_fragment_recycler, parent,
            false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = friendList[position]

        holder.friendImage.setImageBitmap(currentItem.bitmap)
        holder.friendName.text = currentItem.friends.username
        holder.friendEmail.text = currentItem.friends.email

        holder.itemView.setOnClickListener {
            onFriendsFragmentClickCallback.onClickItem(friendList[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int {

        return friendList.size
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        var friendImage : ImageView = itemView.findViewById(R.id.circleImageView)
        var friendName : TextView = itemView.findViewById(R.id.textViewFriendName)
        var friendEmail : TextView = itemView.findViewById(R.id.textViewFriendEmail)

    }
}