package com.jrProfessor.todoapp.screen.home

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.jrProfessor.todoapp.dagger.DaggerBaseActivity
import com.jrProfessor.todoapp.ui.theme.ToDoAppTheme
import com.jrProfessor.todoapp.viewmodel.HomeViewModel

class HomeActivity : DaggerBaseActivity() {

    private val viewmodel by lazy {
        ViewModelProvider(this, viewmodelFactory)[HomeViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ToDoAppTheme {
                HomeScreen(viewmodel)
            }
        }
    }
}