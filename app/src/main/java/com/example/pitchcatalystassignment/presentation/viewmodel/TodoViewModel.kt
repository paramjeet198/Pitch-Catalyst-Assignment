package com.example.pitchcatalystassignment.presentation.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pitchcatalystassignment.data.FirebaseRepository
import com.example.pitchcatalystassignment.domain.model.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TodoViewModel(private val repository: FirebaseRepository) : ViewModel() {

    val todoItems = MutableLiveData<List<TodoItem>>()
    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()


    val selectedItemIds = MutableLiveData<List<TodoItem>>(emptyList())
//    val selectedItemIds: LiveData<List<TodoItem>> = _selectedItemIds

    init {
        loadTodoItems()
    }

    private fun loadTodoItems() {

        viewModelScope.launch {
            try {
                loading.value = true
                val items = repository.getTodoItems()
                todoItems.value = items
            } catch (e: Exception) {
                error.value = e.message
            } finally {
                loading.value = false
            }
        }

    }


    fun addTodoItem(title: String, body: String) {
        viewModelScope.launch {
            try {
                loading.value = true
                repository.addTodoItem(title, body)
                loadTodoItems()
            } catch (e: Exception) {
                error.value = e.message
            } finally {
                loading.value = false
            }
        }
    }

    fun deleteCheckedTodoItems(ids: List<String>) {
        viewModelScope.launch {
            try {
                loading.value = true
                repository.deleteCheckedTodoItems(ids)
                loadTodoItems()
            } catch (e: Exception) {
                error.value = e.message
            } finally {
                loading.value = false
            }
        }
        clearSelectedItems()
    }

    fun updateSelectedItems(ids: List<TodoItem>) {
        selectedItemIds.value = ids
        Log.d("_selectedItemIds", ids.toString())
    }

    fun clearSelectedItems() {
        selectedItemIds.value = emptyList()
    }
}
