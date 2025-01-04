package com.jrProfessor.todoapp

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp

class TodoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.getInstance()
    }
}