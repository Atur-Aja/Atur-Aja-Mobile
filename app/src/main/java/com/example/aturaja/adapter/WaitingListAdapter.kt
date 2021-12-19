package com.example.aturaja.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.aturaja.R
import com.example.aturaja.model.GetAllFriendRequestResponse

class WaitingListAdapter(private val waitingList : ArrayList<GetAllFriendRequestResponse>) : RecyclerView.Adapter<WaitingListAdapter.ViewHolder>() {
    private lateinit var onAcceptClickCallback: OnAcceptClickCallback
    private lateinit var onIgnoreClickCallback: OnIgnoreClickCallback

    fun setOnAcceptClickCallback(onAcceptClickCallback: OnAcceptClickCallback) {
        this.onAcceptClickCallback = onAcceptClickCallback
    }

    fun setOnIgnoreClickCalllback(onIgnoreClickCallback: OnIgnoreClickCallback) {
        this.onIgnoreClickCallback = onIgnoreClickCallback
    }

    interface OnAcceptClickCallback {
        fun onAccpetClicked(data: GetAllFriendRequestResponse)
    }

    interface OnIgnoreClickCallback {
        fun onIgnoreClicked(data: GetAllFriendRequestResponse)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.waiting_list_layout,
            parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = waitingList[position]

        Glide.with(holder.itemView.context)
            .load(R.drawable.a)
            .apply(RequestOptions().override(55, 55))
            .into(holder.friendImage)

        holder.friendName.text = currentItem.username
        holder.friendEmail.text = currentItem.email

        holder.buttonAccept.setOnClickListener{
            onAcceptClickCallback.onAccpetClicked(waitingList[holder.adapterPosition])
        }

        holder.buttonIgnore.setOnClickListener{
            onIgnoreClickCallback.onIgnoreClicked(waitingList[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int {

        return waitingList.size
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        var friendImage : ImageView = itemView.findViewById(R.id.imageViewFriends)
        var friendName : TextView = itemView.findViewById(R.id.textViewFriendName)
        var friendEmail : TextView = itemView.findViewById(R.id.textViewFriendEmail)
        var buttonAccept: Button = itemView.findViewById(R.id.buttonAccept)
        var buttonIgnore: Button = itemView.findViewById(R.id.buttonIgnore)
    }
}
