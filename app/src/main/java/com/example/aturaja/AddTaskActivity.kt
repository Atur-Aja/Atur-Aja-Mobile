package com.example.aturaja


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AddTaskActivity : AppCompatActivity() {

    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<Todo>
    private lateinit var todoName : Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        todoName = arrayOf(
            "tidur",
            "tidur",
        )


        newRecyclerView = findViewById(R.id.toDoRecyclerView)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)

        newArrayList = arrayListOf()
        getUserData()

    }

    private fun getUserData() {

        for(i in todoName.indices){

            val todo = Todo(todoName[i])
            newArrayList.add(todo)

        }

        newRecyclerView.adapter = TodoAdapter(newArrayList)

    }


}