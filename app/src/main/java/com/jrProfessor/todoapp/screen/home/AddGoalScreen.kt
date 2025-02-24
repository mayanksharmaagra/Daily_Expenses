package com.jrProfessor.todoapp.screen.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.jrProfessor.todoapp.R
import com.jrProfessor.todoapp.screen.CustomLoader
import com.jrProfessor.todoapp.screen.common.CommonEditViewWithLabel
import com.jrProfessor.todoapp.screen.common.CustomToolBar
import com.jrProfessor.todoapp.screen.common.IconSpinnerItem
import com.jrProfessor.todoapp.ui.theme.PrimaryColor
import com.jrProfessor.todoapp.utils.AppUtils.CATEGORY
import com.jrProfessor.todoapp.viewmodel.HomeViewModel

@Preview
@Composable
fun PreviewGoalScreen(modifier: Modifier = Modifier) {
    AddGoalScreen(null, null)
}

@Composable
fun AddGoalScreen(
    viewModel: HomeViewModel? = null,
    navController: NavHostController? = null
) {
    var goalAmount by remember { mutableStateOf("") }
    var goalCategory by remember { mutableStateOf("Select Category") }
    var goalName by remember { mutableStateOf("") }
    val context = LocalContext.current
    viewModel?.let {
        val isLoading by viewModel.isLoading.observeAsState(false)
        val errorMessage by viewModel.errorMessage.observeAsState()
        val isSuccess by viewModel.isSuccess.observeAsState(false)

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
        ) {
            val (toolbar, itemField, amountField, categorySpinner, btnAddExpense) = createRefs()
            CustomToolBar(
                Modifier.constrainAs(toolbar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                navController,
                icon = painterResource(R.drawable.ic_arrow_back),
                title = "Add Goal",
                textColor = PrimaryColor
            )
            CommonEditViewWithLabel(modifier = Modifier.constrainAs(itemField) {
                top.linkTo(toolbar.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }, text = goalName, title = "Goal Name", onValueChange = { goalName = it })

            CommonEditViewWithLabel(modifier = Modifier.constrainAs(amountField) {
                top.linkTo(itemField.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
                title = "Goal Amount",
                text = goalAmount,
                keyboardType = KeyboardType.Number,
                onValueChange = { goalAmount = it })

            IconSpinnerItem(
                label = "Goal Category",
                options = CATEGORY,
                selectedOption = goalCategory,
                onOptionSelected = { goalCategory = it },
                modifier = Modifier.constrainAs(categorySpinner) {
                    top.linkTo(amountField.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )

            Button(modifier = Modifier.constrainAs(btnAddExpense) {
                top.linkTo(categorySpinner.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }, onClick = {
                if (goalAmount.isNotEmpty() &&
                    goalCategory.isNotEmpty() &&
                    goalName.isNotEmpty()
                ) {
                    val hashMap = hashMapOf(
                        "amount" to goalAmount,
                        "goalCategory" to goalCategory,
                        "addAmount" to "",
                        "goalName" to goalName,
                    )
                    viewModel.saveGoal(hashMap)
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
                    "Add Goal",
                    fontStyle = FontStyle.Normal,
                    fontSize = 20.sp
                )
            }
        }
        // ✅ Use LaunchedEffect to show toast only once
        LaunchedEffect(isSuccess) {
            if (isSuccess) {
                Toast.makeText(context, "Successfully saved goal", Toast.LENGTH_SHORT).show()
                goalAmount = ""
                goalCategory = ""
                goalName = ""
                navController?.popBackStack()
            }
        }

        LaunchedEffect(errorMessage) {
            errorMessage?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }

        if (isLoading) {
            CustomLoader(true) // Show loading indicator
        }
    }
}