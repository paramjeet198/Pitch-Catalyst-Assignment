package com.example.pitchcatalystassignment.presentation.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.pitchcatalystassignment.databinding.TodoItemBinding
import com.example.pitchcatalystassignment.domain.model.TodoItem
import com.example.pitchcatalystassignment.presentation.viewmodel.TodoViewModel

class TodoAdapter(private var mData: MutableList<TodoItem>, private val viewModel: TodoViewModel) :
    RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    fun updateList(items: List<TodoItem>) {
        mData.clear()
        mData.addAll(items)
        viewModel.clearSelectedItems()
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = TodoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false);
        return TodoViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val item: TodoItem = mData[position]
        with(holder.binding) {
            title.text = item.title /*+ " " + (position + 1)*/
            body.text = item.body
            checkbox.isChecked = item.isChecked
        }

        holder.binding.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            item.isChecked = isChecked
            updateSelectedItems()
        }
    }

    private fun updateSelectedItems() {
        val selectedItems = mData.filter { item -> item.isChecked }
        viewModel.updateSelectedItems(selectedItems)
    }


    class TodoViewHolder(todoBinding: TodoItemBinding) : ViewHolder(todoBinding.root) {
        val binding: TodoItemBinding = todoBinding
    }
}