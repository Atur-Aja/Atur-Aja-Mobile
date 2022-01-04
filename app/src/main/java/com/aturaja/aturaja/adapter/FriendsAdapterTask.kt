package com.aturaja.aturaja.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.aturaja.aturaja.R
import com.aturaja.aturaja.model.ArrayFriendsTask

class FriendsAdapterTask(
    private val friendList: ArrayList<ArrayFriendsTask>
): RecyclerView.Adapter<FriendsAdapterTask.ViewHolder>() {
    private lateinit var onButtonClickCallback: OnButtonCLickCallback

    interface OnButtonCLickCallback {
        fun onClickButton(data: ArrayFriendsTask)
    }

    fun setOnButtonClickCallback(onButtonClickCallback: OnButtonCLickCallback) {
        this.onButtonClickCallback = onButtonClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.friends_recycler_task, parent,
            false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = friendList[position]

        holder.friendImage.setImageBitmap(currentItem.bitmap)

        holder.button.setOnClickListener {
            onButtonClickCallback.onClickButton(friendList[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int {
      return friendList.size
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        var friendImage : ImageView = itemView.findViewById(R.id.img_item_photo)
        var button: ImageButton = itemView.findViewById(R.id.imageButton3)

    }
}