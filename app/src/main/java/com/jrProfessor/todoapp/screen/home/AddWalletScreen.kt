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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.navigation.NavHostController
import com.jrProfessor.todoapp.R
import com.jrProfessor.todoapp.model.GoalsModel
import com.jrProfessor.todoapp.screen.CustomLoader
import com.jrProfessor.todoapp.screen.common.CommonEditViewWithLabel
import com.jrProfessor.todoapp.screen.common.CustomToolBar
import com.jrProfessor.todoapp.screen.common.TextSpinnerItem
import com.jrProfessor.todoapp.ui.theme.PrimaryColor
import com.jrProfessor.todoapp.viewmodel.HomeViewModel

@Preview
@Composable
fun AddWalletPreview(modifier: Modifier = Modifier) {
    AddWalletScreen()
}

@Composable
fun AddWalletScreen(
    viewModel: HomeViewModel? = null,
    navController: NavHostController? = null
) {
    val listsOfGoal: List<GoalsModel> = viewModel?.goals?.value.orEmpty()
    var amount by remember { mutableDoubleStateOf(0.0) }
    var selectedGoal by remember { mutableStateOf("Select Goal") }
    var selectedId by remember { mutableStateOf("") }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel?.getAllGoals()
    }
    viewModel?.let {
        val isLoading by viewModel.isLoading.observeAsState(false)
        val errorMessage by viewModel.errorMessage.observeAsState()
        val isSuccess by viewModel.isSuccess.observeAsState(false)

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
                options = listsOfGoal,
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
                onValueChange = {
                    amount = it.toDouble()
                }
            )
            if (selectedId.isNotEmpty()) {
                Text(
                    text = (listsOfGoal.find { it.id == selectedId }?.amount!! - (listsOfGoal.find { it.id == selectedId }?.addAmount!! - amount)).toDouble().toString(),
                    fontStyle = FontStyle.Normal,
                    fontSize = 20.sp,
                    modifier = Modifier,
                    color = Color.Red
                )
            }


            Button(
                modifier = Modifier,
                onClick = {
                    if (amount != 0.0 && selectedId.isNotEmpty()) {
                        viewModel.saveWalletForGoal(amount, selectedId)
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
            isLoading -> {
                // Show loading indicator when data is being saved
                CustomLoader(true)
            }

            !errorMessage.isNullOrEmpty() -> {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }

            else -> {
                if (isSuccess) {
                    Toast.makeText(
                        context, "Successfully save record", Toast.LENGTH_SHORT
                    ).show()
                    //clear view
                    amount = 0.0
                    selectedGoal = ""
                    navController?.popBackStack()
                }
            }
        }
    }
}