package com.example.pitchcatalystassignment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pitchcatalystassignment.data.FirebaseRepository
import com.example.pitchcatalystassignment.databinding.ActivityMainBinding
import com.example.pitchcatalystassignment.databinding.DialogAddTodoBinding
import com.example.pitchcatalystassignment.presentation.ui.TodoAdapter
import com.example.pitchcatalystassignment.presentation.viewmodel.TodoViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: TodoViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var todoAdapter: TodoAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = FirebaseRepository()
        viewModel = TodoViewModel(repository)
        binding.recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        todoAdapter = TodoAdapter(mutableListOf(), viewModel)
        binding.recyclerView.adapter = todoAdapter


        viewModel.todoItems.observe(this) { items ->
            todoAdapter.updateList(items)
            if (items.isNotEmpty()) {
                binding.status.visibility = View.GONE
            } else {
                binding.status.visibility = View.VISIBLE
            }
        }

        viewModel.loading.observe(this) { isLoading ->
            binding.loadingIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.recyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
//            binding.status.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        viewModel.selectedItemIds.observe(this) { selectedIds ->
            if (selectedIds.isNotEmpty()) {
                binding.fabDelete.show()
                binding.fabAdd.hide()
            } else {
                binding.fabDelete.hide()
                binding.fabAdd.show()
            }
        }

        binding.fabDelete.setOnClickListener(View.OnClickListener {
            confirmDialog()
        })

        binding.fabAdd.setOnClickListener(View.OnClickListener {
            openDialog()
        })


//        viewModel.addTodoItem("New Todo 1", "Todo descriptions")
//        viewModel.addTodoItem("New Todo 2", "Todo descriptionsf")
//        viewModel.addTodoItem("New Todo 3", "Todo description sjk")
//        viewModel.addTodoItem("New Todo 4", "Todo description jsbdfb ")
//        viewModel.addTodoItem("New Todo 5", "Todo description hjbshdf")


    }

    private fun openDialog() {
        val binding = DialogAddTodoBinding.inflate(LayoutInflater.from(this))

        val builder: MaterialAlertDialogBuilder = MaterialAlertDialogBuilder(this)
            .setTitle("Add Note")
            .setView(binding.root)

        val dialog = builder.create()

        binding.submitBtn.setOnClickListener(View.OnClickListener {
            val title = binding.title.text.toString().trim()
            val body = binding.body.text.toString().trim()

            viewModel.addTodoItem(title, body)

            dialog.dismiss()
        })

        dialog.show()

    }

    private fun confirmDialog() {
        val builder = MaterialAlertDialogBuilder(this)
            .setTitle("Are you sure!")
            .setMessage("Are you sure, you want to delete this/these item(s). This action can't be undone.")
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }.setPositiveButton("OK") { dialog, _ ->
                deleteItems()
                dialog.dismiss()
            }.show()
    }

    private fun deleteItems() {
        val selectedIds: List<String>? = viewModel.selectedItemIds.value?.map { todoItem -> todoItem.id }
        Log.d("selectedIds", selectedIds.toString())
        if (selectedIds != null) {
            viewModel.deleteCheckedTodoItems(selectedIds)
            todoAdapter.notifyDataSetChanged()
        }
    }
}