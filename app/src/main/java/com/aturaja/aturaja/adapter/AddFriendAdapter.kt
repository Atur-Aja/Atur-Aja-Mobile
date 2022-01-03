package com.aturaja.aturaja.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aturaja.aturaja.R
import com.aturaja.aturaja.model.FriendsRecyclerAddFriend

class AddFriendAdapter(
    private val addFriendList: ArrayList<FriendsRecyclerAddFriend>
) : RecyclerView.Adapter<AddFriendAdapter.ViewHolder>() {

    private lateinit var onAddClickCallback: OnAddClickCallBack



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.add_friend_layout,
            parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = addFriendList[position]

        holder.friendImage.setImageBitmap(currentItem.bitmap)
        holder.friendName.text = currentItem.data.username
        holder.buttonAccept.setOnClickListener{
            onAddClickCallback.onAddClicked(addFriendList[holder.adapterPosition])
            holder.buttonAccept.isClickable = false
            holder.buttonAccept.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {

        return addFriendList.size
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        var friendImage : ImageView = itemView.findViewById(R.id.imageViewFriends)
        var friendName : TextView = itemView.findViewById(R.id.textViewFriendName)
        var buttonAccept : Button = itemView.findViewById(R.id.buttonAccept)

    }

    fun setOnAddClickCallback(onAddClickCallBack: OnAddClickCallBack){
        this.onAddClickCallback = onAddClickCallBack
    }


    interface OnAddClickCallBack {
        fun onAddClicked (data: FriendsRecyclerAddFriend)
    }
}