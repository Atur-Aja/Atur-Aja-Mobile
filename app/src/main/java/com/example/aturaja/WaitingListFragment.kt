package com.example.aturaja

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aturaja.databinding.FragmentWaitingListBinding


class WaitingListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var arrayList: ArrayList<WaitingList>
    private lateinit var friendName : Array<String>
    private lateinit var friendEmail : Array<String>
    private lateinit var friendImage : Array<Int>
    private var _binding : FragmentWaitingListBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        friendName = arrayOf(
            "Jack Sparrow",
            "Jack Sparrow",
            "Jack Sparrow"

        )

        friendEmail = arrayOf(
            "pirates@gmail.com",
            "pirates@gmail.com",
            "pirates@gmail.com"

        )

        friendImage = arrayOf(
            R.drawable.a,
            R.drawable.b,
            R.drawable.c
        )


        recyclerView = binding.waitingListRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)

        arrayList = arrayListOf()
        getUserData()
    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentWaitingListBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun getUserData() {
        for(i in friendImage.indices){

            val waitingList = WaitingList(friendImage[i],friendName[i],friendEmail[i])
            arrayList.add(waitingList)

        }

        recyclerView.adapter = WaitingListAdapter(arrayList)
    }



}