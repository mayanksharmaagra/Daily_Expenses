package com.jrProfessor.todoapp.viewExtensions

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.runtime.Composable

@Composable
fun Context.hideKeyboard() {
    val inputMethodManager =
        this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val currentFocusView = LocalSoftwareKeyboardController.current
    currentFocusView?.hide()
}