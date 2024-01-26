package com.example.pitchcatalystassignment

import android.app.Application
import com.google.firebase.firestore.FirebaseFirestore

class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()

        FirebaseFirestore.setLoggingEnabled(true)
        FirebaseFirestore.getInstance()
    }
}