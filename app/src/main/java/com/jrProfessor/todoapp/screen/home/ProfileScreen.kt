package com.jrProfessor.todoapp.screen.home

import android.app.Activity
import android.content.Intent
import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.jrProfessor.todoapp.R
import com.jrProfessor.todoapp.model.User
import com.jrProfessor.todoapp.screen.common.ScreenClass
import com.jrProfessor.todoapp.screen.welcome.MainActivity
import com.jrProfessor.todoapp.ui.theme.GrayD6
import com.jrProfessor.todoapp.ui.theme.PrimaryColor
import com.jrProfessor.todoapp.viewmodel.HomeViewModel

@Composable
fun ProfileScreen(viewModel: HomeViewModel?, navController: NavHostController?) {
    val user = viewModel?.getUser()
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        val (box1, userView, categoryView, logoutView, deleteAccountView) = createRefs()
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(color = PrimaryColor)
            .constrainAs(box1) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })

        ProfileCard(user, modifier = Modifier.constrainAs(userView) {
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            top.linkTo(box1.bottom, margin = -(70.dp))
        })
        CategoriesCard(modifier = Modifier.constrainAs(categoryView) {
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            top.linkTo(userView.bottom, margin = 20.dp)
        }, navController)
        LogoutCard(modifier = Modifier.constrainAs(logoutView) {
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            top.linkTo(categoryView.bottom, margin = 20.dp)
        }, viewModel)
        DeleteAccountCard(modifier = Modifier.constrainAs(deleteAccountView) {
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            top.linkTo(logoutView.bottom, margin = 20.dp)
        })
    }
}

@Composable
fun ProfileCard(user: User?, modifier: Modifier) {
    Card(
        modifier = modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp), // High shadow,
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center // Ensures the content inside the Box is centered
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Card(
                    modifier = Modifier
                        .width(75.dp)
                        .height(75.dp),
                    elevation = CardDefaults.cardElevation(1.dp), // High shadow,
                    shape = RoundedCornerShape(50.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Image(
                            painter = painterResource(R.drawable.home_selected),
                            contentDescription = "Todo",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Text(
                    text = user?.name.toString(),
                    style = TextStyle(fontSize = 16.sp, color = GrayD6),
                    modifier = Modifier.padding(vertical = 5.dp)
                )
                Text(
                    text = user?.emailId.toString(),
                    style = TextStyle(fontSize = 14.sp, color = Color.Gray)
                )
            }
        }
    }
}

@Composable
fun CategoriesCard(modifier: Modifier, navController: NavHostController?) {
    Card(modifier = modifier
        .padding(horizontal = 15.dp)
        .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp), // High shadow,
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = {
            navController?.navigate(ScreenClass.AddExpense.route)
        }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.ic_wallet),
                    contentDescription = "Todo",
                    modifier = Modifier
                        .padding(vertical = 5.dp)
                        .weight(.1f)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Wallet",
                    style = TextStyle(fontSize = 16.sp, color = GrayD6),
                    modifier = Modifier
                        .padding(vertical = 5.dp)
                        .weight(.6f)
                )
                Text(
                    text = "Rs. 00.00",
                    style = TextStyle(fontSize = 16.sp, color = GrayD6, textAlign = TextAlign.End),
                    modifier = Modifier
                        .padding(vertical = 5.dp)
                        .weight(.3f)
                )
            }
        }
    }
}

@Composable
fun LogoutCard(modifier: Modifier, viewModel: HomeViewModel?) {
    val context = LocalContext.current
    Card(modifier = modifier
        .padding(horizontal = 15.dp)
        .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp), // High shadow,
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = {
            viewModel?.logout {
                if (it) {
                    Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()
                    context.startActivity(Intent(context, MainActivity::class.java))
                    (context as Activity).finishAffinity()
                }
            }
        }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.ic_logout),
                    contentDescription = "Todo",
                    modifier = Modifier
                        .padding(vertical = 5.dp)
                        .weight(.1f)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Logout",
                    style = TextStyle(fontSize = 16.sp, color = GrayD6),
                    modifier = Modifier
                        .padding(vertical = 5.dp)
                        .weight(.9f)
                )
            }
        }
    }
}

@Composable
fun DeleteAccountCard(modifier: Modifier) {
    Card(
        modifier = modifier
            .padding(horizontal = 15.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp), // High shadow,
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.ic_delete),
                    contentDescription = "Todo",
                    modifier = Modifier
                        .padding(vertical = 5.dp)
                        .weight(.1f)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Delete Account",
                    style = TextStyle(fontSize = 16.sp, color = Color.Red),
                    modifier = Modifier
                        .padding(vertical = 5.dp)
                        .weight(.9f)
                )
            }
        }
    }
}

@Preview
@Composable
fun ProfilePreview(modifier: Modifier = Modifier) {
    ProfileScreen(null, null)
}