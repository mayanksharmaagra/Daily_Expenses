package com.jrProfessor.todoapp.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jrProfessor.todoapp.model.User
import com.jrProfessor.todoapp.repository.UserRepository
import com.jrProfessor.todoapp.utils.AppUtils.DB_NAME
import javax.inject.Inject

class AuthenticationViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    fun signInAccount(
        user: User,
        onSuccess: (User?, String?) -> Unit,
        onError: (User?, String?) -> Unit
    ) {
        userRepository.signInAccount(user, onSuccess, onError)
    }

    fun signUpAccount(user: User, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        userRepository.signUpAccount(user, onSuccess, onError)
    }

    fun isLoggedIn() = userRepository.isLoggedIn()
}