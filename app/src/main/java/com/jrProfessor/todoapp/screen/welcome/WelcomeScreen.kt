package com.jrProfessor.todoapp.screen.welcome

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jrProfessor.todoapp.R
import com.jrProfessor.todoapp.screen.common.ActionButton
import com.jrProfessor.todoapp.screen.home.HomeActivity
import com.jrProfessor.todoapp.ui.theme.PrimaryColor
import com.jrProfessor.todoapp.viewmodel.AuthenticationViewModel
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen(activity: MainActivity, viewmodel: AuthenticationViewModel, onClick: () -> Unit) {
    val isLogged = viewmodel.isLoggedIn()
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(.3f))
        Image(
            painter = painterResource(R.drawable.welcome_logo),
            contentDescription = null
        )
        Text(
            "Lets Manage Expenses.",
            fontSize = 26.sp,
            color = Color.Black,
            modifier = Modifier.padding(10.dp),
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.weight(.5f))
        if (isLogged) {
            LaunchedEffect(Unit) {
                delay(500) // Simulate a loading delay
                activity.startActivity(Intent(context, HomeActivity::class.java))
                activity.finishAffinity()
            }
        } else {
            ActionButton(
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = PrimaryColor
                ),
                title = "Get Started",
                onClick = onClick
            )
        }
    }
}