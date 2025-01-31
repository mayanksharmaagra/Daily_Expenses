package com.jrProfessor.todoapp

import android.app.Application
import android.content.SharedPreferences
import com.jrProfessor.todoapp.dagger.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import javax.inject.Inject

class TodoApplication : DaggerApplication() {

    @Inject
    lateinit var editor:SharedPreferences
    override fun onCreate() {
        super.onCreate()
    }

    private val androidInjector = DaggerAppComponent.builder()
        .application(this)
        .build()
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> = androidInjector
}