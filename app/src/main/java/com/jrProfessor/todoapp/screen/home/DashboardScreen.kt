package com.jrProfessor.todoapp.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.jrProfessor.todoapp.R
import com.jrProfessor.todoapp.model.User
import com.jrProfessor.todoapp.ui.theme.PrimaryColor
import com.jrProfessor.todoapp.viewmodel.HomeViewModel
import java.util.Calendar

@Preview
@Composable
fun DashboardScreenPreview() {
    DashboardScreen()
}

@Composable
fun DashboardScreen(viewModel: HomeViewModel? = null) {
    val user = viewModel?.getUser()
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        val (box1, userHeader) = createRefs()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(color = PrimaryColor)
                .constrainAs(box1) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )
        ShowUserHeader(user, modifier = Modifier.constrainAs(userHeader) {
            top.linkTo(box1.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        })
    }
}

@Composable
fun ShowUserHeader(user: User?, modifier: Modifier) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(20.dp,30.dp),
        elevation = CardDefaults.cardElevation(2.dp), // High shadow
        shape = MaterialTheme.shapes.small, // Rounded corners
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black
        )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 15.dp)
                    .weight(1f)
            ) {
                Text(
                    text = getGreeting(),
                    style = TextStyle(fontSize = 14.sp)
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = user?.name.toString(),
                    style = TextStyle(fontSize = 14.sp)
                )
            }
            Card(
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp),
                elevation = CardDefaults.cardElevation(1.dp), // High shadow,
                shape = MaterialTheme.shapes.extraLarge,
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(R.drawable.home),
                        contentDescription = "User Profile",
                        modifier = Modifier.fillMaxSize().padding(10.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Spacer(modifier = Modifier.width(15.dp))
        }
    }
}

fun getGreeting(): String {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)

    return when {
        hour < 12 -> "Good Morning"
        hour in 12..16 -> "Good Afternoon"
        else -> "Good Evening"
    }
}
