package com.aturaja.aturaja.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aturaja.aturaja.R
import com.aturaja.aturaja.model.TodoItem

class TodoAdapterEditDelete(private val todoList : ArrayList<TodoItem>) : RecyclerView.Adapter<TodoAdapterEditDelete.ViewHolder>(){
    private lateinit var onCLickDeleteTodo: OnClickDeleteTodoEditDelete
    private lateinit var onCheckedTodo: OnCheckedTodo

    interface OnCheckedTodo {
        fun onCheckedItem(data: TodoItem, status: Boolean)
    }

    interface OnClickDeleteTodoEditDelete {
        fun onClickItem(data: TodoItem)
    }

    fun setOnCheckedTodo(onCheckedTodo: OnCheckedTodo) {
        this.onCheckedTodo = onCheckedTodo
    }

    fun setOnClickDeleteTodo(onClickDeleteTodo: OnClickDeleteTodoEditDelete) {
        this.onCLickDeleteTodo = onClickDeleteTodo
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.todo_layout, parent,
            false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = todoList[position]
        val bool = currentItem.status

        holder.todoName.text = currentItem.name
        holder.delButton.setOnClickListener {
            onCLickDeleteTodo.onClickItem(todoList[holder.adapterPosition])
        }
        if (bool != null) {
            holder.checkBox.isChecked = bool == 1
        }

        holder.checkBox.setOnCheckedChangeListener { _, boolean ->
            onCheckedTodo.onCheckedItem(todoList[holder.adapterPosition], boolean)
        }
    }

    override fun getItemCount(): Int {
        return todoList.size

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var todoName : TextView = itemView.findViewById(R.id.tvTodoName)
        var delButton: ImageButton = itemView.findViewById(R.id.deleteTodoButton)
        var checkBox: CheckBox = itemView.findViewById(R.id.cbStatus)
    }
}