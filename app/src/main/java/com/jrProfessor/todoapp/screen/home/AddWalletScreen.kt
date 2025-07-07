package com.jrProfessor.todoapp.screen.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.jrProfessor.todoapp.R
import com.jrProfessor.todoapp.intent.UserExpenseIntent
import com.jrProfessor.todoapp.screen.CustomLoader
import com.jrProfessor.todoapp.screen.bottomnav.BottomNavScreen
import com.jrProfessor.todoapp.screen.common.CommonEditViewWithLabel
import com.jrProfessor.todoapp.screen.common.CustomToolBar
import com.jrProfessor.todoapp.screen.common.TextSpinnerItem
import com.jrProfessor.todoapp.ui.theme.PrimaryColor
import com.jrProfessor.todoapp.viewmodel.HomeViewModel

@Preview
@Composable
fun AddWalletPreview() {
}

@Composable
fun AddWalletScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val goalsState by viewModel.goalsState.collectAsState()
    val walletState by viewModel.saveWalletState.collectAsState()
    var amount by remember { mutableStateOf("0.0") }
    var selectedGoal by remember { mutableStateOf("Select Goal") }
    var selectedId by remember { mutableStateOf("") }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.performUserExpenseIntent(UserExpenseIntent.GetAllGoals)
    }
    viewModel.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
        ) {
            CustomToolBar(
                Modifier,
                navController,
                icon = painterResource(R.drawable.ic_arrow_back),
                title = "Add Amount for Goals",
                textColor = PrimaryColor
            )

            TextSpinnerItem(
                label = "Goal Name",
                options = goalsState.result,
                selectedOption = selectedGoal,
                onOptionSelected = {
                    selectedId = it.id
                    selectedGoal = it.goalName
                },
                modifier = Modifier
            )

            CommonEditViewWithLabel(
                modifier = Modifier,
                title = "Amount",
                text = amount.toString(),
                keyboardType = KeyboardType.Number,
                onValueChange = { amount = it }
            )
            if (selectedId.isNotEmpty() && amount.isNotEmpty()) {
                Text(
                    text = (goalsState.result?.find { it.id == selectedId }?.amount?.toDouble()!! -
                            (goalsState.result?.find { it.id == selectedId }?.addAmount?.toDouble()!! + amount.toDouble()))
                        .toString(),
                    fontStyle = FontStyle.Normal,
                    fontSize = 20.sp,
                    modifier = Modifier,
                    color = Color.Red
                )
            }


            Button(
                modifier = Modifier,
                onClick = {
                    if (amount.isNotEmpty() && amount.toDouble() > 0.0 && selectedId.isNotEmpty()) {
                        viewModel.performUserExpenseIntent(
                            UserExpenseIntent.SaveWallet,
                            amount = amount,
                            id = selectedId
                        )
                    } else {
                        Toast.makeText(
                            context, "Fields are empty.", Toast.LENGTH_SHORT
                        ).show()
                    }
                }, colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White, containerColor = PrimaryColor
                ), shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    "Submit",
                    fontStyle = FontStyle.Normal,
                    fontSize = 20.sp
                )
            }
        }
        when {
            walletState.loading -> {
                CustomLoader(true)
            }

            walletState.success.isNullOrEmpty() == false -> {
                Toast.makeText(
                    context, walletState.success, Toast.LENGTH_SHORT
                ).show()
                //clear view
                amount = "0.0"
                selectedGoal = ""
                viewModel.resetSaveGoalState()
                // Navigate back to dashboard
                navController.popBackStack(BottomNavScreen.Dashboard.route, false)
            }

            !walletState.error.isNullOrEmpty() -> {
                Toast.makeText(context, walletState.error, Toast.LENGTH_SHORT).show()
            }
        }
    }
}