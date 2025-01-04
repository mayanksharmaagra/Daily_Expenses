package com.jrProfessor.todoapp.screen

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.jrProfessor.todoapp.R
import com.jrProfessor.todoapp.model.User
import com.jrProfessor.todoapp.ui.theme.PrimaryColor

@Preview
@Composable
fun SignUpScreenPreview(modifier: Modifier = Modifier) {
//    SignUpScreen()
}


@Composable
fun SignUpScreen() {
    val context = LocalContext.current
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.white))

    ) {
        /**signup related variable*/
        var signUpNameState by remember { mutableStateOf("") }
        var signUpMobileState by remember { mutableStateOf("") }
        var signUpPasswordState by remember { mutableStateOf("") }
        var signUpIsPasswordVisible by remember { mutableStateOf(false) }

        /**signin related variable*/
        var signInMobileState by remember { mutableStateOf("") }
        var signInPasswordState by remember { mutableStateOf("") }
        var signInIsPasswordVisible by remember { mutableStateOf(false) }

        var isLoginView by remember { mutableStateOf(false) }

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
                    .fillMaxWidth()
                    .padding(20.dp)
                    .weight(.8f),
                verticalArrangement = Arrangement.Center
            ) {
                //sign up compose view
                if (!isLoginView) {
                    Column(verticalArrangement = Arrangement.Center) {
                        Text(
                            "Create Account",
                            color = PrimaryColor,
                            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 30.sp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp),
                        )
                        OutlinedTextField(
                            value = signUpNameState,
                            onValueChange = { signUpNameState = it },
                            label = { Text("Full Name") },
                            placeholder = { Text("Full Name...") },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp)
                        )
                        OutlinedTextField(
                            value = signUpMobileState,
                            onValueChange = { number ->
                                if (number.all { it.isDigit() } && number.length <= 10) {
                                    signUpMobileState = number
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
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp),
                            value = signUpPasswordState,
                            onValueChange = {
                                signUpPasswordState = it
                            },
                            label = { Text("Password") },
                            visualTransformation = if (signUpIsPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                val iconId =
                                    if (signUpIsPasswordVisible) R.drawable.eye else R.drawable.hidden
                                val description =
                                    if (signUpIsPasswordVisible) "Hide password" else "Show password"

                                IconButton(onClick = {
                                    signUpIsPasswordVisible = !signUpIsPasswordVisible
                                }) {
                                    Icon(
                                        painter = painterResource(id = iconId),
                                        contentDescription = description,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }
                            },
                            singleLine = true,
                        )
                    }
                }
                //sign in compose view
                if (isLoginView) {
                    Column(verticalArrangement = Arrangement.Center) {
                        Text(
                            "Sign-In",
                            color = PrimaryColor,
                            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 30.sp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp),
                        )
                        OutlinedTextField(
                            value = signInMobileState,
                            onValueChange = { number ->
                                if (number.all { it.isDigit() } && number.length <= 10) {
                                    signInMobileState = number
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
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp),
                            value = signInPasswordState,
                            onValueChange = {
                                signInPasswordState = it
                            },
                            label = { Text("Password") },
                            visualTransformation = if (signInIsPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                val iconId =
                                    if (signInIsPasswordVisible) R.drawable.eye else R.drawable.hidden
                                val description =
                                    if (signInIsPasswordVisible) "Hide password" else "Show password"

                                IconButton(onClick = {
                                    signInIsPasswordVisible = !signInIsPasswordVisible
                                }) {
                                    Icon(
                                        painter = painterResource(id = iconId),
                                        contentDescription = description,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }
                            },
                            singleLine = true,
                        )
                    }
                }
                Button(
                    modifier = Modifier.padding(0.dp),
                    onClick = { isLoginView = !isLoginView },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = PrimaryColor,
                        containerColor = Color.Transparent
                    ),
                ) {
                    Text(
                        if (!isLoginView) "Sign In" else "Create Account",
                        color = PrimaryColor,
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    )
                }

                Spacer(modifier = Modifier.weight(.2f))
                ActionButton(
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        containerColor = PrimaryColor
                    ),
                    title = if (isLoginView) "Sign In" else "Create Account",
                    onClick = {
                        if (!isLoginView) {
                            val user = User(
                                name = signUpNameState,
                                mobile = signUpMobileState,
                                password = signUpPasswordState
                            )
                            signUpAccount(user, context, onSuccess = {
                                isLoginView = true
                            }, onError = {

                            }
                            )
                        } else {

                        }
                    }
                )
            }
        }
    }
}

fun signUpAccount(user: User, context: Context, onSuccess: () -> Unit, onError: () -> Unit) {
    val firebaseDatabase = FirebaseDatabase.getInstance();
    val databaseReference = firebaseDatabase.getReference("ExpensesInfo")

    val userKey = user.mobile
    databaseReference.child(userKey).setValue(user).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            onSuccess()
            Toast.makeText(context, "Your information saved successfully", Toast.LENGTH_SHORT)
                .show()
        } else {
            onError()
            Toast.makeText(
                context,
                "Database Error: ${task.exception?.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}