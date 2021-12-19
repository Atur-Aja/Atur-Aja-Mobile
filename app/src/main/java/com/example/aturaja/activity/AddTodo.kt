package com.example.aturaja.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aturaja.R
import com.example.aturaja.adapter.TodoAdapter
import com.example.aturaja.model.AddTodoResponse
import com.example.aturaja.model.Todo
import com.example.aturaja.network.APIClient
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

//        imgButton.setOnClickListener {
//            var todo = editTExt.text.toString()
//            var angka = 0
//            val arrayOfZeros = Array<String>(10){"aku"}
//
//            todoArrayList.add(Todo(todo))
//            toName.add(todo)
//            toDos.add(todo)
//
//            recycler.adapter = TodoAdapter(todoArrayList)
//        }

        buttonSave.setOnClickListener {
            var apiClietn = APIClient()
            var cobalah = List<String>(toName.size){""}
            var cobaloh: List<String> = toDos
            var iter = 0

            for(i in toName) {
                iter++
            }

            Toast.makeText(this, "${cobalah[0]}", Toast.LENGTH_SHORT).show()

            apiClietn.getApiService(this).createTodo(1, cobaloh)
                .enqueue(object: Callback<AddTodoResponse> {
                    override fun onResponse(
                        call: Call<AddTodoResponse>,
                        response: Response<AddTodoResponse>
                    ) {
                        Log.d("add todo ", "$response")
                        Log.d("add todo ", "berhasil masuk")
                    }

                    override fun onFailure(call: Call<AddTodoResponse>, t: Throwable) {
                        Log.d("add todo ", "$t")
                        Log.d("add todo ", "gagal masuk")
                    }

                })
        }
    }
}
