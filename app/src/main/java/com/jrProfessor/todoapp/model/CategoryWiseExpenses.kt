package com.jrProfessor.todoapp.model

data class CategoryWiseExpenses(
    val expenses: List<ExpensesModel>,
    val category: String,
    val totalAmount: String
)