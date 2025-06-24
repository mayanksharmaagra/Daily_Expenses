package com.jrProfessor.todoapp.screen.home

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.jrProfessor.todoapp.R
import com.jrProfessor.todoapp.model.ExpensesModel
import com.jrProfessor.todoapp.screen.CustomLoader
import com.jrProfessor.todoapp.screen.common.CommonDateTimeViewWithLabel
import com.jrProfessor.todoapp.screen.common.CommonEditViewWithLabel
import com.jrProfessor.todoapp.screen.common.CustomToolBar
import com.jrProfessor.todoapp.screen.common.IconSpinnerItem
import com.jrProfessor.todoapp.ui.theme.PrimaryColor
import com.jrProfessor.todoapp.utils.AppUtils.CATEGORY
import com.jrProfessor.todoapp.viewmodel.HomeViewModel

@Preview
@Composable
fun PreviewScreen(modifier: Modifier = Modifier) {
    AddExpenseScreen(null, null, null, null)
}

@Composable
fun AddExpenseScreen(
    viewModel: HomeViewModel? = null,
    id: String?,
    expenseModel: ExpensesModel?,
    navController: NavHostController? = null
) {
    var amount by remember { mutableDoubleStateOf(expenseModel?.amount ?: 0.0) }
    var selectedCategory by remember { mutableStateOf(expenseModel?.category ?: "Select Category") }
    var itemName by remember { mutableStateOf(expenseModel?.itemName ?: "") }
    var dateValue by remember { mutableStateOf(expenseModel?.dateValue ?: "") }
    var timeValue by remember { mutableStateOf(expenseModel?.timeValue ?: "") }
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
            val (toolbar, amountField, categorySpinner, itemField, dateField, timeField, btnAddExpense, loadingIndicator) = createRefs()
            CustomToolBar(
                Modifier.constrainAs(toolbar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                navController,
                icon = painterResource(R.drawable.ic_arrow_back),
                title = "Add Expenses",
                textColor = PrimaryColor
            )
            CommonEditViewWithLabel(
                modifier = Modifier.constrainAs(amountField) {
                    top.linkTo(toolbar.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                title = "Amount",
                text = amount.toString(),
                keyboardType = KeyboardType.Number,
                onValueChange = { amount = it.toDouble() })
            IconSpinnerItem(
                label = "Expense Category",
                options = CATEGORY,
                selectedOption = selectedCategory,
                onOptionSelected = { selectedCategory = it },
                modifier = Modifier.constrainAs(categorySpinner) {
                    top.linkTo(amountField.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )

            CommonEditViewWithLabel(modifier = Modifier.constrainAs(itemField) {
                top.linkTo(categorySpinner.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }, text = itemName, title = "Item Name", onValueChange = { itemName = it })

            CommonDateTimeViewWithLabel(
                modifier = Modifier.constrainAs(dateField) {
                    top.linkTo(itemField.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }, title = "Date",
                icon = R.drawable.ic_calendar,
                value = dateValue,
                onValueChange = { dateValue = it })

            CommonDateTimeViewWithLabel(
                modifier = Modifier.constrainAs(timeField) {
                    top.linkTo(dateField.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                title = "Time",
                icon = R.drawable.ic_time,
                value = timeValue,
                onValueChange = { timeValue = it })

            Button(
                modifier = Modifier.constrainAs(btnAddExpense) {
                top.linkTo(timeField.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }, onClick = {
                if (amount.toString().isNotEmpty() &&
                    selectedCategory.isNotEmpty() &&
                    itemName.isNotEmpty() &&
                    dateValue.isNotEmpty() &&
                    timeValue.isNotEmpty()
                ) {
                    if (id.isNullOrEmpty()) {
                        val hashMap = ExpensesModel(
                            amount = amount,
                            category = selectedCategory,
                            itemName = itemName,
                            dateValue = dateValue,
                            timeValue = timeValue
                        )
                        viewModel.saveExpenses(hashMap)
                    } else {
                        val model = ExpensesModel(
                            amount = amount,
                            category = selectedCategory,
                            itemName = itemName,
                            dateValue = dateValue,
                            timeValue = timeValue,
                            id = id
                        )
                        viewModel.updateExpenses(id, model)
                    }
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
                    if (id.isNullOrEmpty()) "Submit" else "Update",
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
                    selectedCategory = ""
                    itemName = ""
                    dateValue = ""
                    timeValue = ""
                    navController?.popBackStack()
                }
            }
        }
    }
}