package com.jrProfessor.todoapp

import android.app.Application
import android.content.SharedPreferences
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class TodoApplication : Application() {

    @Inject
    lateinit var editor:SharedPreferences
    override fun onCreate() {
        super.onCreate()
    }
}