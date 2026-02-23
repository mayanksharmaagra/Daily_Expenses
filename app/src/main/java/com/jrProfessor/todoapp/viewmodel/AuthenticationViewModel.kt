package com.jrProfessor.todoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jrProfessor.todoapp.intent.ResponseState
import com.jrProfessor.todoapp.intent.UserAuthenticationIntent
import com.jrProfessor.todoapp.model.User
import com.jrProfessor.todoapp.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _state = MutableStateFlow(ResponseState<User>())
    val authenticationState: StateFlow<ResponseState<User>> = _state
    fun userAuthentication(intent: UserAuthenticationIntent, user: User) {
        when (intent) {
            UserAuthenticationIntent.UserSignUp -> {
                signUpAccount(user)
            }

            UserAuthenticationIntent.UserSignIn -> {
                signInAccount(user)
            }
        }
    }

    private fun signInAccount(user: User) {
        viewModelScope.launch {
            userRepository.signInAccount(user)
                .onStart {
                    _state.value = _state.value.copy(loading = true)
                }.catch { error ->
                    _state.value = ResponseState(loading = false, error = error.message)
                }.collect { result ->
                    result.onSuccess {
                        _state.value = ResponseState(
                            loading = false, result = it, success = "Login Successful"
                        )
                    }
                    result.onFailure {
                        _state.value = ResponseState(loading = false, error = it.message)
                    }
                }
        }
    }

    private fun signUpAccount(user: User) {
        viewModelScope.launch {
            userRepository.signUpAccount(user)
                .onStart {
                    _state.value = _state.value.copy(loading = true)
                }.catch {
                    _state.value = ResponseState(loading = false, error = it.message)
                }.collect { result ->
                    result.onSuccess {
                        _state.value = ResponseState(loading = false, success = it)
                    }
                    result.onFailure {
                        _state.value = ResponseState(loading = false, error = it.message)
                    }
                }
        }
    }

    fun isLoggedIn() = userRepository.isLoggedIn()
}