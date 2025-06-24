package com.jrProfessor.todoapp.model

data class GoalsModel(
    val amount: Double = 0.0,
    val addAmount: Double = 0.0,
    val goalCategory: String = "",
    val goalName: String = "",
    var id: String = ""
)