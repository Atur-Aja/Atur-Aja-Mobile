package com.example.aturaja.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aturaja.R
import com.example.aturaja.model.Todo

class TodoAdapter (private val todoList : ArrayList<String>) : RecyclerView.Adapter<TodoAdapter.ViewHolder>(){
    private lateinit var onCLickDeleteTodo: OnClickDeleteTodo

    interface OnClickDeleteTodo {
        fun onClickItem(data: String)
    }

    fun setOnClickDeleteTodo(onClickDeleteTodo: OnClickDeleteTodo) {
        this.onCLickDeleteTodo = onClickDeleteTodo
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.todo_layout, parent,
            false)

        return TodoAdapter.ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = todoList[position]

        holder.todoName.text = currentItem
        holder.delButton.setOnClickListener {
            onCLickDeleteTodo.onClickItem(todoList[holder.adapterPosition])
        }

    }

    override fun getItemCount(): Int {
        return todoList.size

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var todoName : TextView = itemView.findViewById(R.id.tvTodoName)
        var delButton: ImageButton = itemView.findViewById(R.id.deleteTodoButton)
    }
}