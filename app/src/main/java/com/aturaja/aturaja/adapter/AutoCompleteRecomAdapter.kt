package com.aturaja.aturaja.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.aturaja.aturaja.R
import com.aturaja.aturaja.model.RekomendasiItem

class AutoCompleteRecomAdapter(
    context: Context,
    resource: Int,
    objects: MutableList<RekomendasiItem>
) : ArrayAdapter<RekomendasiItem>(context, resource, objects) {
    private var recomendationList = ArrayList<RekomendasiItem>()
    private lateinit var onRecomClickCallback: OnRecomCLickCallback

    interface OnRecomCLickCallback {
        fun onCLickRecom(data: RekomendasiItem)
    }

    fun setOnRecomClickCallback(onRecomCLickCallback: OnRecomCLickCallback) {
        this.onRecomClickCallback = onRecomCLickCallback
    }

    override fun getFilter(): Filter {
        return filter
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = LayoutInflater.from(context).inflate(
            R.layout.recomendation_layout, parent, false
        )

        val tvTimeFrom = view.findViewById<TextView>(R.id.tv_time_from_recom_add_schedule)
        val tvTimeTo = view.findViewById<TextView>(R.id.tv_time_to_recom_add_schedule)
        val item = getItem(position)

        tvTimeFrom?.text = item?.startTime
        tvTimeTo?.text = item?.endTime
        view.setOnClickListener {
            item?.let { it1 -> onRecomClickCallback.onCLickRecom(item) }
        }

        return view
    }

    private val filter = object: Filter() {
        override fun performFiltering(p0: CharSequence?): FilterResults {
            val results = FilterResults()
            val recomendationSuggestion = ArrayList<RekomendasiItem>()

            recomendationSuggestion.addAll(recomendationList)
            results.values = recomendationSuggestion
            results.count = recomendationSuggestion.size
            return results
        }

        override fun publishResults(p0: CharSequence?, result: FilterResults?) {
            clear()
            addAll(result?.values as ArrayList<RekomendasiItem>)
            notifyDataSetChanged()
        }

        override fun convertResultToString(resultValue: Any?): CharSequence {
            var kalimat = "From : ${(resultValue as RekomendasiItem).startTime} To : ${(resultValue as RekomendasiItem).endTime}"
            return kalimat
        }

    }
}