package com.jrProfessor.todoapp.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import com.jrProfessor.todoapp.R
import com.jrProfessor.todoapp.ui.theme.PrimaryColor

@Preview
@Composable
fun SignUpScreenPreview(modifier: Modifier = Modifier) {
    SignUpScreen()
}


@Composable
fun SignUpScreen(modifier: Modifier = Modifier) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.white))

    ) {
        var nameState by remember { mutableStateOf("") }
        var mobileState by remember { mutableStateOf("") }
        Column {
            Image(
                painter = painterResource(R.drawable.wave),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(.2f),
                contentScale = ContentScale.FillHeight,
            )
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .weight(.6f), verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "Create Account",
                    color = PrimaryColor,
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 30.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp),
                )
                OutlinedTextField(
                    value = nameState,
                    onValueChange = { nameState = it },
                    label = { Text("Full Name") },
                    placeholder = { Text("Full Name...") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp)
                )
                OutlinedTextField(
                    value = mobileState,
                    onValueChange = { number ->
                        if (number.all { it.isDigit() } && number.length <= 10) {
                            mobileState = number
                        }
                    },
                    label = { Text("Mobile no.") },
                    placeholder = { Text("Mobile no.") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.weight(.2f))
                ActionButton(
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        containerColor = PrimaryColor
                    ),
                    title = "Create Account"
                )
            }
        }
    }
}