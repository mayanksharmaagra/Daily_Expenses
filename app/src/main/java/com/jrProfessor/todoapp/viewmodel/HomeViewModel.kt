package com.jrProfessor.todoapp.viewmodel

import androidx.lifecycle.ViewModel
import com.jrProfessor.todoapp.repository.UserRepository
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    fun isLoggedIn() = userRepository.isLoggedIn()
    fun getUser() = userRepository.getUser()
    fun logout(onLogout: (Boolean) -> Unit) {
        userRepository.logout()
        onLogout(true)
    }

    fun saveExpenses(
        expenses: HashMap<String, String>,
        onSuccess: (Boolean) -> Unit,
        onError: (Boolean, String) -> Unit
    ) {
        userRepository.saveExpenses(
            expenses,
            onSuccess,
            onError
        )
    }
}