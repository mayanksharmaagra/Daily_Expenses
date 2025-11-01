package com.jrProfessor.todoapp.screen.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.jrProfessor.todoapp.R
import com.jrProfessor.todoapp.intent.UserExpenseIntent
import com.jrProfessor.todoapp.model.GoalsModel
import com.jrProfessor.todoapp.model.User
import com.jrProfessor.todoapp.screen.CustomLoader
import com.jrProfessor.todoapp.screen.bottomnav.BottomNavScreen
import com.jrProfessor.todoapp.screen.common.CircularProgressBar
import com.jrProfessor.todoapp.screen.common.ScreenClass
import com.jrProfessor.todoapp.ui.theme.CardColor
import com.jrProfessor.todoapp.ui.theme.CardTextColor
import com.jrProfessor.todoapp.ui.theme.PrimaryColor
import com.jrProfessor.todoapp.utils.AppUtils
import com.jrProfessor.todoapp.viewmodel.HomeViewModel
import java.util.Calendar
import androidx.compose.runtime.*

@Preview
@Composable
fun DashboardScreenPreview() {

}

@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun DashboardScreen(
    viewModel: HomeViewModel = hiltViewModel(), navHostController: NavHostController
) {
    viewModel.let {

        val user by viewModel.user.observeAsState()//observeAsState use for live data
        val goalsState by viewModel.goalsState.collectAsState()//collectAsState use for flow data

        /*LaunchedEffect(navHostController) {
            navHostController.currentBackStackEntryFlow.collect { backStackEntry ->
                val destination = backStackEntry.destination.route
                if (destination == BottomNavScreen.Dashboard.route) {
                    viewModel.performUserExpenseIntent(UserExpenseIntent.GetAllGoals)
                }
            }
        }*/
        LaunchedEffect(Unit) {
            viewModel.performUserExpenseIntent(UserExpenseIntent.GetAllGoals)
        }

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
        ) {
            val (box1, userHeader, goal) = createRefs()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .background(color = PrimaryColor)
                    .constrainAs(box1) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    })
            ShowUserHeader(user, modifier = Modifier.constrainAs(userHeader) {
                top.linkTo(box1.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })
            when {
                goalsState.loading -> {
                    CustomLoader(true)
                }

                !goalsState.success.isNullOrEmpty() -> {
                    AddGoal(modifier = Modifier.constrainAs(goal) {
                        top.linkTo(box1.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }, goalsState.result, navigate = {
                        navHostController?.navigate(it)
                    }, deleteGoal = {
                        viewModel.performUserExpenseIntent(UserExpenseIntent.DeleteGoals, it)
                    })
                }

                !goalsState.error.isNullOrEmpty() -> {
                    Column(
                        modifier = Modifier.fillMaxSize(), // Fill the entire screen
                        verticalArrangement = Arrangement.Center, // Center vertically
                        horizontalAlignment = Alignment.CenterHorizontally // Center horizontally
                    ) {
                        androidx.compose.material3.Text(
                            text = goalsState.error ?: "Something went wrong",
                            color = Color.Red,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AddGoal(
    modifier: Modifier,
    goalsList: List<GoalsModel>?,
    navigate: (String) -> Unit?,
    deleteGoal: (String?) -> Unit
) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Row {
            Row(Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.target),
                    contentDescription = "Back Button",
                    tint = PrimaryColor,
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Goal Countdown", modifier = Modifier.weight(1f), style = TextStyle(
                        color = PrimaryColor, fontSize = 20.sp, fontWeight = FontWeight.Bold
                    )
                )
                Icon(
                    painter = painterResource(R.drawable.more),
                    contentDescription = "Back Button",
                    tint = PrimaryColor,
                    modifier = Modifier.clickable {
                        navigate(ScreenClass.AddGoal.route)
                    })
                Spacer(modifier = Modifier.width(10.dp))
            }
        }
        Spacer(Modifier.height(10.dp))
        if (goalsList?.isEmpty() == true) {
            Text("No goals added yet.", color = Color.Gray)
        } else {
            goalsList?.let {
                LazyColumn {
                    items(goalsList) { goal ->
                        GoalItem(goal, deleteGoal)
                    }
                }
            }
        }
    }
}

@Composable
fun GoalItem(goal: GoalsModel?, deleteGoal: (String?) -> Unit) {
    val _progress = (goal?.amount?.toFloat()?.let {
        goal.addAmount.toFloat().div(it)
    })?.times(100)
    val progress by remember { mutableFloatStateOf(_progress ?: 0f) }
    val startAngle by remember { mutableFloatStateOf(0f) }
    val progressBarWidth by remember { mutableStateOf(6.dp) }
    val backgroundProgressBarWidth by remember { mutableStateOf(8.dp) }
    val roundBorder by remember { mutableStateOf(true) }
    val animProgress by animateFloatAsState(progress)
    Log.e(
        "TAG",
        "GoalItem: ${goal?.goalCategory} == " + AppUtils.getIconForCategory(goal?.goalCategory)
    )
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(), // Padding for the card
        shape = RoundedCornerShape(8.dp), // Rounded corners for the card
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = CardColor)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(
                Modifier
                    .padding(8.dp)
                    .weight(1f)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = goal?.goalName ?: "Item Name",
                        color = AppUtils.pieChartColors[goal?.goalCategory] ?: CardTextColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Image(
                        modifier = Modifier
                            .size(12.dp)
                            .clickable {

                            },
                        painter = painterResource(R.drawable.ic_edit),
                        contentDescription = "Close Icon",
                        colorFilter = ColorFilter.tint(
                            AppUtils.pieChartColors[goal?.goalCategory] ?: CardTextColor
                        )
                    )
                }
                if (goal!=null && goal.addAmount.isNullOrEmpty()==false && goal.addAmount.toDouble() > 0.0) {
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "${AppUtils.getAmount(goal.addAmount.toDouble())} out of ${
                            AppUtils.getAmount(
                                goal.amount.toDouble()
                            )
                        }",
                        color = AppUtils.pieChartColors[goal?.goalCategory] ?: CardTextColor,
                        fontSize = 10.sp
                    )
                }
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Row(
                        modifier = Modifier
                            .background(
                                color = AppUtils.pieChartColors[goal?.goalCategory]
                                    ?: CardTextColor,
                                shape = RoundedCornerShape(15.dp) // Rounded corners
                            )
                            .padding(8.dp, 6.dp), verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(
                                AppUtils.getIconForCategory(goal?.goalCategory)
                            ),
                            tint = Color.White,
                            contentDescription = "Icon",
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = goal?.goalCategory ?: "Category Name",
                            color = Color.White,
                            fontSize = 10.sp
                        )
                    }
                    if (goal?.addAmount == goal?.amount) {
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "You can buy it now!",
                            color = AppUtils.pieChartColors[goal?.goalCategory] ?: CardTextColor,
                            fontSize = 10.sp
                        )
                    }
                }
            }/*progress bar*/
            Row(
                Modifier.padding(8.dp, 6.dp)
            ) {
                CircularProgressBar(
                    modifier = Modifier.size(60.dp),
                    progress = animProgress,
                    progressMax = 100f,
                    progressBarColor = AppUtils.pieChartColors[goal?.goalCategory] ?: PrimaryColor,
                    progressBarWidth = progressBarWidth,
                    backgroundProgressBarColor = AppUtils.pieChartColors[goal?.goalCategory]?.copy(
                        alpha = 0.15f
                    ) ?: PrimaryColor.copy(alpha = 0.15f),
                    backgroundProgressBarWidth = backgroundProgressBarWidth,
                    roundBorder = roundBorder,
                    startAngle = startAngle
                )
                Spacer(modifier = Modifier.width(8.dp))
                Image(
                    modifier = Modifier.clickable {
                        deleteGoal(goal?.id)
                    },
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = "Close Icon",
                    colorFilter = ColorFilter.tint(
                        AppUtils.pieChartColors[goal?.goalCategory] ?: CardTextColor
                    )
                )
            }
        }
    }
}

@Composable
fun ShowUserHeader(user: User?, modifier: Modifier) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp, 30.dp),
        elevation = CardDefaults.cardElevation(2.dp), // High shadow
        shape = MaterialTheme.shapes.small, // Rounded corners
        colors = CardDefaults.cardColors(
            containerColor = Color.White, contentColor = Color.Black
        )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 15.dp)
                    .weight(1f)
            ) {
                Text(
                    text = getGreeting(), style = TextStyle(fontSize = 14.sp)
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = user?.name.toString(), style = TextStyle(fontSize = 14.sp)
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
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
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
