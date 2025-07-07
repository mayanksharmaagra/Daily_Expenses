package com.jrProfessor.todoapp.repository

import com.jrProfessor.todoapp.model.CategoryWiseExpenses
import com.jrProfessor.todoapp.model.ExpensesModel
import com.jrProfessor.todoapp.model.GoalsModel
import com.jrProfessor.todoapp.model.User
import kotlinx.coroutines.flow.Flow


interface UserRepository {

    fun isLoggedIn(): Boolean

    fun getUser(): User?

    fun logout()

    fun saveUser(user: User)

    fun signInAccount(user: User): Flow<Result<User>>

    fun signUpAccount(user: User): Flow<Result<String>>

    fun saveExpenses(expenses: ExpensesModel): Flow<Result<String>>

    fun getAllExpenses(): Flow<Result<List<ExpensesModel>>>

    fun deleteCategory(expensesId: String): Flow<Result<String>>

    fun updateExpenses(expensesId: String, model: ExpensesModel): Flow<Result<String>>

    fun fetchExpensesByCategory(): Flow<Result<List<CategoryWiseExpenses>>>

    fun saveGoal(goal: HashMap<String, Any>): Flow<Result<String>>

    fun getAllGoals(): Flow<Result<List<GoalsModel>>>

    fun deleteGoal(goalId: String?): Flow<Result<String>>

    fun saveWalletForGoal(amount: String, goalId: String): Flow<Result<String>>
}