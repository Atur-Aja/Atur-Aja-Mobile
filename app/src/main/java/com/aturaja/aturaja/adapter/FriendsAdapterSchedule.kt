package com.aturaja.aturaja.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aturaja.aturaja.R
import com.aturaja.aturaja.model.GetFriendResponse

class FriendsAdapterSchedule(private val friendList: ArrayList<GetFriendResponse>) : RecyclerView.Adapter<FriendsAdapterSchedule.ViewHolder>() {
    private lateinit var onFriendsRecyclerClickCallback: OnFriendsRecyclerClickCallback
    private var TAG = "FriendsAdapter"
    interface OnFriendsRecyclerClickCallback {
        fun onClickItem(data: GetFriendResponse)
    }

    fun setOnFriendsRecyclerClickCallback(onFriendsRecyclerClickCallback: OnFriendsRecyclerClickCallback) {
        this.onFriendsRecyclerClickCallback = onFriendsRecyclerClickCallback
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.imageViewFriends)
        var tvUsername: TextView = itemView.findViewById(R.id.textViewFriendName)
        var button: ImageButton = itemView.findViewById(R.id.deleteFriendsRecycler)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.friends_layout_recycler, parent,
            false
        )

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = friendList[position]

        holder.image.setImageResource(R.drawable.c)
        holder.tvUsername.text = currentItem.username
        holder.button.setOnClickListener{
            onFriendsRecyclerClickCallback.onClickItem(friendList[holder.adapterPosition])
//            Log.d(TAG, "${friendList[holder.adapterPosition]}")
        }
    }

    override fun getItemCount(): Int {
        return friendList.size
    }
}