package com.jrProfessor.todoapp.intent

sealed class UserExpenseIntent {
    object SaveGoal: UserExpenseIntent()
    object GetAllGoals: UserExpenseIntent()
    object DeleteGoals: UserExpenseIntent()

    object SaveWallet: UserExpenseIntent()

    object SaveExpense: UserExpenseIntent()
    object UpdateExpense: UserExpenseIntent()
    object GetAllExpenses: UserExpenseIntent()
    object FetchExpenseByCategory: UserExpenseIntent()

    object DeleteCategory: UserExpenseIntent()
}