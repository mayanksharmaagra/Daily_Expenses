package com.jrProfessor.todoapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jrProfessor.todoapp.model.CategoryWiseExpenses
import com.jrProfessor.todoapp.model.ExpensesModel
import com.jrProfessor.todoapp.model.GoalsModel
import com.jrProfessor.todoapp.model.User
import com.jrProfessor.todoapp.repository.UserRepository
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    fun isLoggedIn() = userRepository.isLoggedIn()
    private val _expenses = MutableLiveData<List<ExpensesModel>>()
    val expenses: LiveData<List<ExpensesModel>> = _expenses
    private val _goals = MutableLiveData<List<GoalsModel>>()
    val goals: LiveData<List<GoalsModel>> = _goals
    private val _categoryWiseExpenses = MutableLiveData<List<CategoryWiseExpenses>>()
    val categoryWiseExpenses: LiveData<List<CategoryWiseExpenses>> = _categoryWiseExpenses

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _user = MutableLiveData<User>(userRepository.getUser())
    val user: LiveData<User> = _user
    fun logout(onLogout: (Boolean) -> Unit) {
        userRepository.logout()
        onLogout(true)
    }


    fun fetchExpensesByCategory() {
        _isLoading.value = true
        userRepository.fetchExpensesByCategory(onResult = {
            _categoryWiseExpenses.value = it
            _isLoading.value = false
        }, onError = { _, message ->
            _isLoading.value = false
            _errorMessage.value = message
        })
    }

    fun saveExpenses(expenses: ExpensesModel) {
        _isLoading.value = true

        userRepository.saveExpenses(
            expenses,
            onSuccess = {
                _isSuccess.value = true
                _isLoading.value = false
            },
            onError = { status, message ->
                _errorMessage.value = message
                _isLoading.value = false
            }
        )
    }

    fun getAllExpenses() {
        _isLoading.value = true

        userRepository.getAllExpenses(
            onSuccess = { list ->
                _expenses.value = list
                _isLoading.value = false
            },
            onError = { _, message ->
                _errorMessage.value = message
                _isLoading.value = false
            }
        )
    }

    fun deleteCategory(expensesId: String) {
        _isLoading.value = true
        userRepository.deleteCategory(expensesId,
            onSuccess = {
                _expenses.value = _expenses.value?.filterNot  { data->data.id==expensesId }
                _isLoading.value = false
            }, onError = { message ->
                _errorMessage.value = message
                _isLoading.value = false
            })
    }

    fun updateExpenses(id: String, model: ExpensesModel) {
        _isLoading.value = true
        userRepository.updateExpenses(id, model, onSuccess = {
            _isSuccess.value = true
            _isLoading.value = false
        }, onError = { status, message ->
            _errorMessage.value = message
            _isLoading.value = false

        })

    }

    fun saveGoal(goal: HashMap<String, Any>) {
        _isLoading.value = true

        userRepository.saveGoal(
            goal,
            onSuccess = {
                _isSuccess.value = true
                _isLoading.value = false
            },
            onError = { status, message ->
                _errorMessage.value = message
                _isLoading.value = false
            }
        )
    }

    fun getAllGoals() {
        _isLoading.value = true

        userRepository.getAllGoals(
            onSuccess = { list ->
                _goals.value = list
                _isLoading.value = false
            },
            onError = { _, message ->
                _errorMessage.value = message
                _isLoading.value = false
            }
        )
    }

    fun deleteGoal(goalId: String?) {
        _isLoading.value = true
        userRepository.deleteGoal(goalId,
            onSuccess = {
                _goals.value = _goals.value?.filterNot { it.id == goalId }
                _isLoading.value = false
            },
            onError = { _, message ->
                _errorMessage.value = message
                _isLoading.value = false
            }
        )
    }

    fun saveWalletForGoal(amount: Double, goalId: String) {
        _isLoading.value = true

        userRepository.saveWalletForGoal(
            amount,goalId,
            onSuccess = {
                _isSuccess.value = true
                _isLoading.value = false
            },
            onError = { status, message ->
                _errorMessage.value = message
                _isLoading.value = false
            }
        )
    }
}