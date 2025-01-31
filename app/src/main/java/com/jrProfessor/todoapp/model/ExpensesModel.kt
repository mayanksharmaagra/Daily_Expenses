package com.jrProfessor.todoapp.model

data class ExpensesModel(
    val amount: Double,
    val category: String,
    val itemName: String,
    val dateValue: String,
    val timeValue: String,
    var id: String? = null
)