package com.jrProfessor.todoapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.jrProfessor.todoapp.intent.ResponseState
import com.jrProfessor.todoapp.intent.UserExpenseIntent
import com.jrProfessor.todoapp.model.CategoryWiseExpenses
import com.jrProfessor.todoapp.model.ExpensesModel
import com.jrProfessor.todoapp.model.GoalsModel
import com.jrProfessor.todoapp.model.User
import com.jrProfessor.todoapp.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    fun isLoggedIn() = userRepository.isLoggedIn()

    private val _goals = MutableStateFlow(ResponseState<List<GoalsModel>>())
    val goalsState: StateFlow<ResponseState<List<GoalsModel>>> = _goals

    private val _saveWallet = MutableStateFlow(ResponseState<JsonObject>())
    val saveWalletState: StateFlow<ResponseState<JsonObject>> = _saveWallet

    private val _saveGoal = MutableStateFlow(ResponseState<JsonObject>())
    val saveGoalState: StateFlow<ResponseState<JsonObject>> = _saveGoal

    private val _updateAndSaveExpense = MutableStateFlow(ResponseState<JsonObject>())
    val updateAndSaveExpense: StateFlow<ResponseState<JsonObject>> = _updateAndSaveExpense

    private val _expenses = MutableStateFlow(ResponseState<List<ExpensesModel>>())
    val expensesState: StateFlow<ResponseState<List<ExpensesModel>>> = _expenses

    private val _categoryWiseExpenses =
        MutableStateFlow(ResponseState<List<CategoryWiseExpenses>>())

    val categoryWiseExpensesState: StateFlow<ResponseState<List<CategoryWiseExpenses>>> =
        _categoryWiseExpenses

    private val _user = MutableLiveData<User>(userRepository.getUser())
    val user: LiveData<User> = _user

    fun logout(onLogout: (Boolean) -> Unit) {
        userRepository.logout()
        onLogout(true)
    }

    fun performUserExpenseIntent(
        intent: UserExpenseIntent,
        id: String? = null,
        amount: String = "0.0",
        goal: HashMap<String, Any>? = null,
        model: ExpensesModel? = null
    ) {
        Log.i("TAG", "performUserExpenseIntent: $intent")
        when (intent) {
            is UserExpenseIntent.GetAllGoals -> getAllGoals()

            is UserExpenseIntent.DeleteGoals -> deleteGoal(id)

            is UserExpenseIntent.SaveWallet -> saveWallet(amount, id)

            is UserExpenseIntent.SaveGoal -> saveGoal(goal)

            is UserExpenseIntent.UpdateExpense -> updateExpenses(id, model)

            is UserExpenseIntent.GetAllExpenses -> getAllExpense()

            is UserExpenseIntent.DeleteCategory -> deleteCategory(id)

            is UserExpenseIntent.SaveExpense -> saveExpense(model)

            is UserExpenseIntent.FetchExpenseByCategory -> fetchExpensesByCategory()
        }
    }

    private fun deleteGoal(id: String?) {
        viewModelScope.launch {
            userRepository.deleteGoal(id).onStart {
                _goals.value = _goals.value.copy(loading = true)
            }.catch {
                _goals.value = ResponseState(loading = false, error = it.message)
            }.collect { result ->
                result.onSuccess {
                    _goals.value = ResponseState(
                        loading = false,
                        result = _goals.value.result?.filterNot { it.id == id },
                        success = "Successfully delete goal"
                    )
                }
                result.onFailure {
                    _goals.value = ResponseState(loading = false, error = it.message)
                }
            }
        }
    }

    private fun saveWallet(amount: String, id: String?) {
        viewModelScope.launch {
            if (amount!= "0.0" && id != null) {
                userRepository.saveWalletForGoal(
                    amount, id
                ).onStart {
                    _saveWallet.value = _saveWallet.value.copy(loading = true)
                }.catch {
                    _saveWallet.value = ResponseState(loading = false, error = it.message)
                }.collect { result ->
                    result.onSuccess {
                        _saveWallet.value = ResponseState(loading = false, success = it)
                    }
                    result.onFailure {
                        _saveWallet.value = ResponseState(loading = false, error = it.message)
                    }
                }
            }
        }
    }

    private fun saveGoal(goal: HashMap<String, Any>?) {
        viewModelScope.launch {
            if (goal != null) {
                userRepository.saveGoal(goal).onStart {
                    _saveGoal.value = _saveGoal.value.copy(loading = true)
                }.catch {
                    _saveGoal.value = ResponseState(loading = false, error = it.message)
                }.collect { result ->
                    result.onSuccess {
                        _saveGoal.value = ResponseState(loading = false, success = it)
                    }
                    result.onFailure {
                        _saveGoal.value = ResponseState(loading = false, error = it.message)
                    }
                }
            }
        }
    }

    private fun fetchExpensesByCategory() {
        viewModelScope.launch {
            userRepository.fetchExpensesByCategory()
                .onStart {
                    _categoryWiseExpenses.value = _categoryWiseExpenses.value.copy(loading = true)
                }.catch {
                    _categoryWiseExpenses.value = ResponseState(error = it.message, loading = false)
                }.collect { result ->
                    result.onSuccess {
                        _categoryWiseExpenses.value =
                            ResponseState(loading = false, result = it, success = "Fetch records")
                    }
                    result.onFailure {
                        _categoryWiseExpenses.value =
                            ResponseState(error = it.message, loading = false)
                    }
                }
        }
    }

    private fun updateExpenses(id: String?, model: ExpensesModel?) {
        viewModelScope.launch {
            if (id != null && model != null) {
                userRepository.updateExpenses(id, model).onStart {
                    _updateAndSaveExpense.value = _updateAndSaveExpense.value.copy(loading = true)
                }.catch {
                    _updateAndSaveExpense.value = ResponseState(loading = false, error = it.message)
                }.collect { result ->
                    result.onSuccess {
                        _updateAndSaveExpense.value = ResponseState(loading = false, success = it)
                    }
                    result.onFailure {
                        _updateAndSaveExpense.value =
                            ResponseState(loading = false, error = it.message)
                    }
                }
            }
        }
    }

    private fun deleteCategory(id: String?) {
        viewModelScope.launch {
            if (id.isNullOrEmpty() == false) {
                userRepository.deleteCategory(id).onStart {
                    _expenses.value = _expenses.value.copy(loading = true)
                }.catch {
                    _expenses.value = ResponseState(error = it.message, loading = false)
                }.collect { result ->
                    result.onSuccess {
                        _expenses.value = ResponseState(
                            result = _expenses.value.result?.filterNot { data -> data.id == id },
                            loading = false,
                            success = "Successfully deleted"
                        )
                    }
                    result.onFailure {
                        _expenses.value = ResponseState(error = it.message, loading = false)
                    }
                }
            }
        }
    }

    private fun getAllExpense() {
        viewModelScope.launch {
            userRepository.getAllExpenses().onStart {
                _expenses.value = _expenses.value.copy(loading = true)
            }.catch {
                _expenses.value = ResponseState(error = it.message, loading = false)
            }.collect { result ->
                result.onSuccess {
                    _expenses.value = ResponseState(
                        result = it, loading = false, success = "Successfully fetch all records"
                    )
                }
                result.onFailure {
                    _expenses.value = ResponseState(error = it.message, loading = false)
                }
            }
        }
    }

    private fun saveExpense(model: ExpensesModel?) {
        viewModelScope.launch {
            if (model != null) {
                userRepository.saveExpenses(model).onStart {
                    _updateAndSaveExpense.value = _updateAndSaveExpense.value.copy(loading = true)
                }.catch {
                    _updateAndSaveExpense.value = ResponseState(error = it.message, loading = false)
                }.collect { result ->
                    result.onSuccess {
                        _updateAndSaveExpense.value = ResponseState(loading = false, success = it)
                    }
                    result.onFailure {
                        _updateAndSaveExpense.value =
                            ResponseState(error = it.message, loading = false)
                    }
                }
            }
        }
    }

    private fun getAllGoals() {
        viewModelScope.launch {
            userRepository.getAllGoals().onStart {
                _goals.value = _goals.value.copy(loading = true)
            }.catch {
                _goals.value = ResponseState(loading = false, error = it.message)
            }.collect { result ->
                result.onSuccess {
                    _goals.value =
                        ResponseState(loading = false, result = it, success = "Fetch goals")
                }
                result.onFailure {
                    _goals.value = ResponseState(loading = false, error = it.message)
                }
            }
        }
    }

    fun resetSaveGoalState() {
        _saveGoal.value = ResponseState() // Reset to default empty state
    }
}