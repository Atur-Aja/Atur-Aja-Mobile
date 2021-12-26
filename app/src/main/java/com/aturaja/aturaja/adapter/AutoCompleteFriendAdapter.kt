package com.aturaja.aturaja.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.aturaja.aturaja.R
import com.aturaja.aturaja.model.GetFriendResponse
import java.util.*


//class AutoCompleteFriendAdapter(
//    context: Context,
//    resource: Int,
//    dataList: ArrayList<GetFriendResponse>
//) : ArrayAdapter<GetFriendResponse>(context, resource, dataList) {
//    var TAG = "adapter"
//    private var inflater: LayoutInflater
//    private var listFriends = dataList
//
//    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View? {
//        Log.d(TAG, "$listFriends")
//        return getCustomView(position, convertView, parent)
//    }
//
//    override fun getCount(): Int {
//        return listFriends.size
//    }
//
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        Log.d(TAG, "$listFriends")
//        return getCustomView(position, convertView, parent)!!
//    }
//
//    private fun getCustomView(position: Int, convertView: View?, parent: ViewGroup): View? {
//        var view = convertView
//        var data = listFriends.get(position)
//        var holder: ViewHolder
//
//        Log.d(TAG, "$listFriends")
//
//        if (view == null) {
//            holder = ViewHolder()
//            view = inflater.inflate(R.layout.friends_layout, parent, false)
//            holder.name = view.findViewById(R.id.textViewFriendName)
//            holder.email = view.findViewById(R.id.textViewFriendEmail)
//            view.tag = holder
//        } else {
//            holder = view.tag as ViewHolder
//        }
//
//        holder.name.text = data.username
//        holder.email.text = data.email
//
//        return view
//    }
//
//    inner class ViewHolder() {
//        lateinit var name: TextView
//        lateinit var email: TextView
//        lateinit var image: Drawable
//    }
//
//    init {
//        listFriends = dataList
//        inflater = (context as Activity).layoutInflater
//    }
//}

//class AutoCompleteFriendAdapter(
//    context: Context?,
//    textViewResourceId: Int,
//    objects: List<GetFriendResponse>
//) :
//    ArrayAdapter<GetFriendResponse>(context!!, textViewResourceId, objects) {
//    private var listEntry: GetFriendResponse? = null
//    private var tvName: TextView? = null
//    private var tvEmail: TextView? = null
//    private var categoryIcon: ImageView? = null
//    private var countryEntryList: List<GetFriendResponse> = ArrayList<GetFriendResponse>()
//    override fun getCount(): Int {
//        return countryEntryList.size
//    }
//
//    override fun getItem(position: Int): GetFriendResponse{
//        val journalEntry: GetFriendResponse = countryEntryList[position]
//        Log.d(
//            tag,
//            "*-> Retrieving JournalEntry @ position: " + position.toString() + " : " + journalEntry.toString()
//        )
//        return journalEntry
//    }
//
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        var row = convertView
//        val inflater = LayoutInflater.from(context)
//        if (row == null) {
//            row = inflater.inflate(com.example.aturaja.R.layout.friends_layout, parent, false)
//        }
//        listEntry = countryEntryList[position]
//        val searchItem: String = listEntry!!.username
//        val email: String = listEntry!!.email
//        tvName = row!!.findViewById<View>(com.example.aturaja.R.id.textViewFriendName) as TextView
//        tvEmail = row!!.findViewById<View>(com.example.aturaja.R.id.textViewFriendEmail) as TextView
//        tvName!!.text = searchItem
//        tvEmail!!.text = email
//
//        // Get a reference to ImageView holder
////        categoryIcon = row.findViewById<View>(R.id.category_icon) as ImageView
////        categoryIcon!!.setImageBitmap(listEntry.image)
//        return row
//    }
//
//    companion object {
//        private const val tag = "SearchItemArrayAdapter"
//    }
//
//    /**
//     *
//     * @param context
//     * @param textViewResourceId
//     * @param objects
//     */
//    init {
//        countryEntryList = objects
//        Log.d(
//            tag,
//            "Search List -> journalEntryList := $countryEntryList"
//        )
//    }
//}

class AutoCompleteFriendAdapter(
    context: Context,
    resource: Int,
    objects: MutableList<GetFriendResponse>
) : ArrayAdapter<GetFriendResponse>(context, resource, objects) {
    var TAG = "adapter"

    private lateinit var onFriendsClickCallback: OnFriendsClickCallback
    private var friendList: ArrayList<GetFriendResponse> = ArrayList<GetFriendResponse>(objects)

    interface OnFriendsClickCallback {
        fun onClickFriends(data: GetFriendResponse)
    }

    fun setOnFriendsClickCallback(onFriendsClickCallback: OnFriendsClickCallback) {
        this.onFriendsClickCallback = onFriendsClickCallback
    }

    override fun getFilter(): Filter {
        return filter
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = LayoutInflater.from(context).inflate(
            R.layout.friends_layout, parent, false
        )

        val textViewName = view.findViewById<TextView>(R.id.textViewFriendName)
        val textViewEmali = view.findViewById<TextView>(R.id.textViewFriendEmail)
        val item = getItem(position)

        textViewName?.text = item?.username
        textViewEmali?.text = item?.email
        view.setOnClickListener {
            item?.let { it1 -> onFriendsClickCallback.onClickFriends(it1) }
        }

        return view
    }

    private val filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()
            val friendsSuggestion = ArrayList<GetFriendResponse>()
            val filterPattern: String

            if (constraint == null || constraint.isEmpty()) {
                friendsSuggestion.addAll(friendList)
            } else {
                filterPattern = constraint.toString().lowercase().trim()

                for (item in friendList) {
                    if (item.username.lowercase().contains(filterPattern)) {
                        friendsSuggestion.add(item)
                    }
                }
            }
            results.values = friendsSuggestion
            results.count = friendsSuggestion.size

            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults) {
            clear()
            addAll(results.values as ArrayList<GetFriendResponse>)
            notifyDataSetChanged()
        }

        override fun convertResultToString(resultValue: Any?): CharSequence {
            return (resultValue as GetFriendResponse).username
        }
    }
}