package com.jrProfessor.todoapp.screen.welcome

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.jrProfessor.todoapp.R
import com.jrProfessor.todoapp.model.User
import com.jrProfessor.todoapp.screen.CustomLoader
import com.jrProfessor.todoapp.screen.common.ActionButton
import com.jrProfessor.todoapp.screen.home.HomeActivity
import com.jrProfessor.todoapp.ui.theme.PrimaryColor
import com.jrProfessor.todoapp.utils.CustomToast
import com.jrProfessor.todoapp.viewmodel.AuthenticationViewModel


@Preview
@Composable
fun SignUpScreenPreview(modifier: Modifier = Modifier) {

}


@Composable
fun SignUpScreen(activity: MainActivity, viewmodel: AuthenticationViewModel) {
    val context = LocalContext.current
    /**signup related variable*/
    var signUpNameState by remember { mutableStateOf("") }
    var signUpMobileState by remember { mutableStateOf("") }
    var signUpEmailState by remember { mutableStateOf("") }
    var signUpPasswordState by remember { mutableStateOf("") }
    var signUpIsPasswordVisible by remember { mutableStateOf(false) }

    /**signin related variable*/
    var signInEmailState by remember { mutableStateOf("") }
    var signInPasswordState by remember { mutableStateOf("") }
    var signInIsPasswordVisible by remember { mutableStateOf(false) }

    var showToast by remember { mutableStateOf(false) }
    var isLoginView by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf("") }
    var loadingState by remember { mutableStateOf(false) }  // To track the loading state
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.white))

    ) {

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
                                .padding(2.dp)
                        )
                        OutlinedTextField(
                            value = signUpEmailState,
                            onValueChange = {
                                signUpEmailState = it
                            },
                            label = { Text("Email") },
                            placeholder = { Text("Email...") },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(2.dp),
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
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
                                .padding(2.dp),
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                        )
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(2.dp),
                            value = signUpPasswordState,
                            onValueChange = { password ->
                                if (password.length <= 12) {
                                    signUpPasswordState = password
                                }
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
                            value = signInEmailState,
                            onValueChange = {
                                signInEmailState = it
                            },
                            label = { Text("Email") },
                            placeholder = { Text("Email...") },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(2.dp),
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
                        )
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(2.dp),
                            value = signInPasswordState,
                            onValueChange = { password ->
                                if (password.length <= 12) {
                                    signInPasswordState = password
                                }
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
                        loadingState=true
                        if (!isLoginView) {
                            val user = User(
                                name = signUpNameState,
                                mobile = signUpMobileState,
                                password = signUpPasswordState,
                                emailId = signUpEmailState
                            )
                            viewmodel.signUpAccount(user,
                                onSuccess = {
                                    loadingState=false
                                    toastMessage = it
                                    showToast = true
                                    isLoginView = true
                                },
                                onError = {
                                    loadingState=false
                                    toastMessage = it
                                    showToast = true
                                }
                            )
                        } else {
                            val user = User(
                                emailId = signInEmailState,
                                password = signInPasswordState
                            )
                            viewmodel.signInAccount(user,
                                onSuccess = { user, _ ->
                                    loadingState=false
                                    if (user != null) {
                                        val intent = Intent(context, HomeActivity::class.java)
                                        context.startActivity(intent)
                                        activity.finishAffinity()
                                    }
                                },
                                onError = { _, message ->
                                    loadingState=false
                                    toastMessage = message.orEmpty()
                                    showToast = true
                                }
                            )
                        }
                    }
                )
            }
        }
        if (showToast) {
            CustomToast(
                message = toastMessage,
                iconRes = R.drawable.ic_error,
                onDismiss = { showToast = false }
            )
        }
        CustomLoader(loadingState)
    }
}