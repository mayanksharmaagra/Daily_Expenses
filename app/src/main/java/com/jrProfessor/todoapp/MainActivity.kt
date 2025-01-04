package com.jrProfessor.todoapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.jrProfessor.todoapp.model.User
import com.jrProfessor.todoapp.screen.SignUpScreen
import com.jrProfessor.todoapp.screen.WelcomeScreen
import com.jrProfessor.todoapp.ui.theme.ToDoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ToDoAppTheme {
                MainApp()
            }
        }
    }
    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    private fun MainApp() {
        val navHost = rememberNavController()
        NavHost(
            navController = navHost,
            startDestination = NavGraph.Welcome.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(500)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(500)) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(500)) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(500)) }
        ) {
            composable(NavGraph.Welcome.route) {
                WelcomeScreen(onClick ={
                    navHost.navigate(NavGraph.SignUp.route)
                })
            }
            composable(NavGraph.SignUp.route) {
                SignUpScreen()
            }
        }
    }
    fun signUpAccount(user: User) {
        val firebaseDatabase = FirebaseDatabase.getInstance();
        val databaseReference = firebaseDatabase.getReference("ExpensesInfo")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                databaseReference.setValue(user)
                Log.e("TAG", "onDataChange: Your information save successfully" )
//            Toast.makeText(context,"Your information save successfully",Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("TAG", "onDataChange: ${Gson().toJson(error)}")
//            Toast.makeText(context,"Database Error",Toast.LENGTH_SHORT).show()
            }
        })
    }
}