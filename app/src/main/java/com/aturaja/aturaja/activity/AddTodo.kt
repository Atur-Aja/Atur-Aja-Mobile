package com.aturaja.aturaja.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aturaja.aturaja.R
import com.aturaja.aturaja.model.AddTodoResponse
import com.aturaja.aturaja.model.Todo
import com.aturaja.aturaja.network.APIClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddTodo : AppCompatActivity() {
    private lateinit var buttonSave: Button
    private lateinit var recycler: RecyclerView
    private lateinit var imgButton: ImageButton
    private lateinit var editTExt: EditText
    private var todoArrayList = ArrayList<Todo>()
    private var todoName = mutableListOf<String>()
    private var toName = ArrayList<String>()
    private var toDos = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_todo)

        buttonSave = findViewById(R.id.buttonSave)
        recycler = findViewById(R.id.toDoRecyclerView)
        imgButton = findViewById(R.id.abTodo)
        editTExt = findViewById(R.id.editTextAddTodo)

        recycler = findViewById(R.id.toDoRecyclerView)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.setHasFixedSize(true)


        buttonSave.setOnClickListener {

        }
    }
}
