package com.jrProfessor.todoapp.screen.common

sealed class ScreenClass(val route: String) {
    object AddExpense : ScreenClass(route = "add_expense")
}