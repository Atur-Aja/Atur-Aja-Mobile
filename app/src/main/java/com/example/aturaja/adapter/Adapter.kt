package com.example.aturaja.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aturaja.R
import com.example.aturaja.model.ListSchedule

class Adapter(private val context: Context,
              private val dataSet: List<ListSchedule>
): RecyclerView.Adapter<Adapter.ItemViewHolder>(){

    class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.item_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_schedule, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataSet[position]

        holder.textView.text = context.resources.getString(item.stringResourceId)
    }

    override fun getItemCount() = dataSet.size
}