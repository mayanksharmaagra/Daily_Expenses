package com.jrProfessor.todoapp.utils

import java.util.UUID

object AppUtils {
    val DB_NAME="ExpensesInfo"
    val EXPENSES_TABLE="Expense_Table"
    val USERS="users"
    fun generateRandomId(): String {
        return UUID.randomUUID().toString().replace("-", "").take(32)
    }
}