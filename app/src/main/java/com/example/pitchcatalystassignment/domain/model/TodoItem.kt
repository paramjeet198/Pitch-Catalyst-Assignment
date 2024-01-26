package com.example.pitchcatalystassignment.domain.model

data class TodoItem(
    val id: String = "",
    val title: String = "",
    val body: String = "",
    var isChecked: Boolean = false
)