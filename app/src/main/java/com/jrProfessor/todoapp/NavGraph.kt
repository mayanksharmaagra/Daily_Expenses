package com.jrProfessor.todoapp


sealed class NavGraph(val route:String) {
    data object Welcome:NavGraph("welcome_screen")
    data object SignUp:NavGraph("signup_screen")
}