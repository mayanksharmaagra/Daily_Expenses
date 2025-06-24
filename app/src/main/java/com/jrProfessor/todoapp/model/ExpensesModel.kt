package com.jrProfessor.todoapp.model

data class ExpensesModel(
    val amount: Double = 0.0,
    val category: String = "",
    val itemName: String = "",
    val dateValue: String = "",
    val timeValue: String = "",
    var id: String = ""
)