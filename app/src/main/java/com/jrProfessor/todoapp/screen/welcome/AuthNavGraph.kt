package com.jrProfessor.todoapp.screen.welcome


sealed class AuthNavGraph(val route:String) {
    data object Welcome: AuthNavGraph("welcome_screen")
    data object SignUp: AuthNavGraph("signup_screen")
}