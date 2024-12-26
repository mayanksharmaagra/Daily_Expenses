package com.jrProfessor.todoapp.screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*

@Composable
fun EditTextWithLabel() {
    var text by remember { mutableStateOf("Name") }

    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        label = { Text("Enter your text") },
        placeholder = { Text("Type here...") },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}