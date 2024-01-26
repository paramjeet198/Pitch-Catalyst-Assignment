package com.example.pitchcatalystassignment.data

import com.example.pitchcatalystassignment.domain.model.TodoItem
import com.example.pitchcatalystassignment.domain.repository.TodoRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.UUID

class FirebaseRepository() : TodoRepository {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionName = "Todo";


    override suspend fun getTodoItems(): List<TodoItem> {
        val collectionReference = firestore.collection(collectionName)
        val querySnapshot = collectionReference.get().await()

        return querySnapshot.documents.mapNotNull { document ->
            document.toObject(TodoItem::class.java)
        }
    }

    override suspend fun addTodoItem(title: String, body: String) {
        val item = TodoItem(
            id = UUID.randomUUID().toString(),
            title = title,
            body = body,
            isChecked = false
        )

        val collectionReference = firestore.collection(collectionName)
        collectionReference.add(item).await()
    }

    override suspend fun deleteCheckedTodoItems(ids: List<String>) {
        val collectionReference = firestore.collection(collectionName)
        val batch = FirebaseFirestore.getInstance().batch()

        ids.forEach { id ->
            val query = collectionReference.whereEqualTo("id", id)
            val querySnapshot = query.get().await()
            querySnapshot.documents.forEach { document ->
                batch.delete(document.reference)
            }
        }

        batch.commit().await()
    }
}