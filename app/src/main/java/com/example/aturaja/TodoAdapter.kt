package com.example.aturaja

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TodoAdapter (private val todoList : ArrayList<Todo>) : RecyclerView.Adapter<TodoAdapter.ViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.todo_layout, parent,
            false)

        return TodoAdapter.ViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = todoList[position]

        holder.todoName.text = currentItem.toDoName

    }

    override fun getItemCount(): Int {

        return todoList.size

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var todoName : TextView = itemView.findViewById(R.id.tvTodoName)

    }
}