package com.jrProfessor.todoapp.intent

sealed class UserAuthenticationIntent {
    object UserSignUp: UserAuthenticationIntent()
    object UserSignIn: UserAuthenticationIntent()
}