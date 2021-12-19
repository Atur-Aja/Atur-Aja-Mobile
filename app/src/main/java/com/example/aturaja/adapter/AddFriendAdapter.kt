package com.example.aturaja.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aturaja.R
import com.example.aturaja.model.GetSearchResponseItem

class AddFriendAdapter(private val addFriendList: ArrayList<GetSearchResponseItem>) : RecyclerView.Adapter<AddFriendAdapter.ViewHolder>() {

    private lateinit var onAddClickCallback: OnAddClickCallBack



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.add_friend_layout,
            parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = addFriendList[position]

        holder.friendImage.setImageResource(R.drawable.a)
        holder.friendName.text = currentItem.username
        holder.buttonAccept.setOnClickListener{
            onAddClickCallback.onAddClicked(addFriendList[holder.adapterPosition])
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
        fun onAddClicked (data : GetSearchResponseItem)
    }
}