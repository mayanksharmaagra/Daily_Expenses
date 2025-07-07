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
import androidx.compose.runtime.collectAsState
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.jrProfessor.todoapp.R
import com.jrProfessor.todoapp.intent.UserAuthenticationIntent
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
fun SignUpScreen(activity: MainActivity, viewmodel: AuthenticationViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val state by viewmodel.authenticationState.collectAsState()

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

    var isLoginView by remember { mutableStateOf(false) }
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
                    SignUpForm(
                        signUpNameState,
                        { signUpNameState = it },
                        signUpEmailState,
                        { signUpEmailState = it },
                        signUpMobileState,
                        {
                            if (it.all { ch -> ch.isDigit() } && it.length <= 10) signUpMobileState =
                                it
                        },
                        signUpPasswordState,
                        { if (it.length <= 12) signUpPasswordState = it },
                        signUpIsPasswordVisible,
                        { signUpIsPasswordVisible = !signUpIsPasswordVisible })

                }
                //sign in compose view
                else {
                    SignInForm(
                        signInEmailState,
                        { signInEmailState = it },
                        signInPasswordState,
                        { if (it.length <= 12) signInPasswordState = it },
                        signInIsPasswordVisible,
                        { signInIsPasswordVisible = !signInIsPasswordVisible })
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
                                password = signUpPasswordState,
                                emailId = signUpEmailState
                            )
                            viewmodel.userAuthentication(UserAuthenticationIntent.UserSignUp, user)
                        } else {
                            val user = User(
                                emailId = signInEmailState,
                                password = signInPasswordState
                            )
                            viewmodel.userAuthentication(UserAuthenticationIntent.UserSignIn, user)
                        }
                    }
                )
            }
        }
        when {
            state.loading -> {
                CustomLoader(true)
            }

            state.success != null -> {
                if (isLoginView) {
                    if (state.result != null) {
                        val intent = Intent(context, HomeActivity::class.java)
                        context.startActivity(intent)
                        activity.finishAffinity()
                    }
                } else {
                    isLoginView=true
                }
                CustomToast(
                    message = state.success!!,
                    iconRes = R.drawable.ic_error,
                    onDismiss = {

                    }
                )
            }

            state.error != null -> {
                CustomLoader(false)
                CustomToast(
                    message = state.error!!,
                    iconRes = R.drawable.ic_error,
                    onDismiss = {

                    }
                )
            }

        }
    }
}

@Composable
fun SignInForm(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    isPasswordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit
) {
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
            value = email,
            onValueChange = onEmailChange,
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
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Password") },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val iconId =
                    if (isPasswordVisible) R.drawable.eye else R.drawable.hidden
                val description =
                    if (isPasswordVisible) "Hide password" else "Show password"

                IconButton(onClick = onTogglePasswordVisibility) {
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

@Composable
fun SignUpForm(
    name: String,
    onNameChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    mobile: String,
    onMobileChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    isPasswordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit
) {
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
            value = name,
            onValueChange = onNameChange,
            label = { Text("Full Name") },
            placeholder = { Text("Full Name...") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp)
        )
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            placeholder = { Text("Email...") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
        )
        OutlinedTextField(
            value = mobile,
            onValueChange = onMobileChange,
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
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Password") },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val iconId =
                    if (isPasswordVisible) R.drawable.eye else R.drawable.hidden
                val description =
                    if (isPasswordVisible) "Hide password" else "Show password"

                IconButton(onClick = onTogglePasswordVisibility) {
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