package com.jrProfessor.todoapp.screen.welcome

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jrProfessor.todoapp.ui.theme.ToDoAppTheme
import com.jrProfessor.todoapp.viewmodel.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    lateinit var navHost: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ToDoAppTheme {
                MainApp()
            }
        }
    }

    @Composable
    private fun MainApp() {
        navHost = rememberNavController()
        NavHost(
            navController = navHost, startDestination = AuthNavGraph.Welcome.route
        ) {
            composable(AuthNavGraph.Welcome.route) {
                WelcomeScreen(this@MainActivity) {
                    navHost.navigate(AuthNavGraph.SignUp.route)
                }
            }
            composable(AuthNavGraph.SignUp.route) {
                SignUpScreen(this@MainActivity)
            }
        }
    }
}