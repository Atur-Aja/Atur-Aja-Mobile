package com.aturaja.aturaja.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aturaja.aturaja.R
import com.aturaja.aturaja.model.AppItem
import com.aturaja.aturaja.model.DetailItem
import com.google.android.material.switchmaterial.SwitchMaterial

class AppListAdapter(private val appList: ArrayList<AppItem>): RecyclerView.Adapter<AppListAdapter.ViewHolder>() {
    private lateinit var onSwitchCheckedCallback: OnSwitchCheckedCallback

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView = itemView.findViewById(R.id.app_name)
        var imgPhoto: ImageView = itemView.findViewById(R.id.app_icon)
        var switch: SwitchMaterial = itemView.findViewById(R.id.switch_app)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_app, parent, false
        )

        return ViewHolder(itemView)
    }

    fun setOnSwitchCheckedCallback(onSwitchCheckedCallback: OnSwitchCheckedCallback) {
        this.onSwitchCheckedCallback = onSwitchCheckedCallback
    }

    interface OnSwitchCheckedCallback {
        fun onSwitchChecked(status: Boolean, data: DetailItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val appItem = appList[position]

        holder.tvName.text = appItem.detailApp.name
        holder.imgPhoto.setImageDrawable(appItem.icon)

        holder.switch.isChecked = false

        holder.switch.setOnCheckedChangeListener { _, status ->
            onSwitchCheckedCallback.onSwitchChecked(status, appItem.detailApp)
        }
    }

    override fun getItemCount() = appList.size
}