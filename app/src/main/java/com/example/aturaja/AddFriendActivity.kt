package com.example.aturaja

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AddFriendActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var arrayList: ArrayList<AddFriend>
    private lateinit var friendName : Array<String>
    private lateinit var friendEmail : Array<String>
    private lateinit var friendImage : Array<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friend)

        friendName = arrayOf(
            "Jack Sparrow",
            "Jack Sparrow",
            "Jack Sparrow",
            "Jack Sparrow",
            "Jack Sparrow",
            "Jack Sparrow",
            "Jack Sparrow",
            "Jack Sparrow",
            "Jack Sparrow"

        )

        friendEmail = arrayOf(
            "pirates@gmail.com",
            "pirates@gmail.com",
            "pirates@gmail.com",
            "pirates@gmail.com",
            "pirates@gmail.com",
            "pirates@gmail.com",
            "pirates@gmail.com",
            "pirates@gmail.com",
            "pirates@gmail.com"

        )

        friendImage = arrayOf(
            R.drawable.a,
            R.drawable.b,
            R.drawable.c,
            R.drawable.a,
            R.drawable.b,
            R.drawable.c,
            R.drawable.a,
            R.drawable.b,
            R.drawable.c
        )


        recyclerView = findViewById(R.id.addFriendRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        arrayList = arrayListOf()
        getUserData()

    }

    private fun getUserData() {
        for(i in friendImage.indices){

            val addFriend = AddFriend(friendImage[i],friendName[i],friendEmail[i])
            arrayList.add(addFriend)

        }

        recyclerView.adapter = AddFriendAdapter(arrayList)
    }

    fun backOnClick(view: android.view.View) {
        finish()
    }
}