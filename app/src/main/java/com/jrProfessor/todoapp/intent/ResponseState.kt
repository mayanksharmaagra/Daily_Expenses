package com.jrProfessor.todoapp.intent


data class ResponseState<T>(
    val loading: Boolean = false,
    val success: String? = null,
    val result: T?=null,
    val error: String? = null,
)