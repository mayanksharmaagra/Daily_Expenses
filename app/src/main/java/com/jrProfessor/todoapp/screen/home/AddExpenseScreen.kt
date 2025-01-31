package com.jrProfessor.todoapp.screen.home

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.jrProfessor.todoapp.R
import com.jrProfessor.todoapp.screen.CustomLoader
import com.jrProfessor.todoapp.ui.theme.PrimaryColor
import com.jrProfessor.todoapp.viewmodel.HomeViewModel
import java.util.Calendar

@Preview
@Composable
fun PreviewScreen(modifier: Modifier = Modifier) {
    AddExpenseScreen()
}

@Composable
fun AddExpenseScreen(viewModel: HomeViewModel? = null) {
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var itemName by remember { mutableStateOf("") }
    var dateValue by remember { mutableStateOf("") }
    var timeValue by remember { mutableStateOf("") }
    var loadingState by remember { mutableStateOf(false) }  // To track the loading state
    val context = LocalContext.current
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        val (toolbar, amountField, categoryField, itemField, dateField, timeField, btnAddExpense, loadingIndicator) = createRefs()
        CustomToolBar(Modifier.constrainAs(toolbar) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        })
        CommonEditViewWithLabel(modifier = Modifier.constrainAs(amountField) {
            top.linkTo(toolbar.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        },
            title = "Amount",
            text = amount.toString(),
            keyboardType = KeyboardType.Number,
            onValueChange = { amount = it })
        CommonEditViewWithLabel(modifier = Modifier.constrainAs(categoryField) {
            top.linkTo(amountField.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }, text = category, title = "Category", onValueChange = { category = it })

        CommonEditViewWithLabel(modifier = Modifier.constrainAs(itemField) {
            top.linkTo(categoryField.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }, text = itemName, title = "Item Name", onValueChange = { itemName = it })

        CommonDateTimeViewWithLabel(modifier = Modifier.constrainAs(dateField) {
            top.linkTo(itemField.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        },
            title = "Date",
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

        Button(modifier = Modifier.constrainAs(btnAddExpense) {
            top.linkTo(timeField.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }, onClick = {
            if (amount.isNotEmpty() &&
                category.isNotEmpty() &&
                itemName.isNotEmpty() &&
                dateValue.isNotEmpty() &&
                timeValue.isNotEmpty()
            ) {
                loadingState = true
                val hashMap = hashMapOf(
                    "amount" to amount,
                    "category" to category,
                    "itemName" to itemName,
                    "dateValue" to dateValue,
                    "timeValue" to timeValue
                )
                viewModel?.saveExpenses(hashMap, onSuccess = { isSuccess ->
                    loadingState = false
                    if (isSuccess) {
                        Toast.makeText(
                            context, "Successfully save record", Toast.LENGTH_SHORT
                        ).show()
                        //clear view
                        amount = ""
                        category = ""
                        itemName = ""
                        dateValue = ""
                        timeValue = ""
                    }
                }, onError = { isSuccess, message ->
                    loadingState = false
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                })
            } else {
                Toast.makeText(
                    context, "Fields are empty.", Toast.LENGTH_SHORT
                ).show()
            }
        }, colors = ButtonDefaults.buttonColors(
            contentColor = Color.White, containerColor = PrimaryColor
        ), shape = RoundedCornerShape(12.dp)
        ) {
            Text("Submit", fontStyle = FontStyle.Normal, fontSize = 20.sp)
        }
        // Show loading indicator when data is being saved
        CustomLoader(loadingState)
    }
}

@Composable
fun CommonDateTimeViewWithLabel(
    modifier: Modifier, title: String, icon: Int, value: String, onValueChange: (String) -> Unit
) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {

        Text(title, style = TextStyle(color = Color.Black, fontSize = 16.sp))
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    if (title == "Date") {
                        chooseDate(context, onValueChange)
                    } else {
                        chooseTime(context, onValueChange)
                    }
                }) {
            Text(
                if (value.isNotEmpty()) value else if (title == "Date") "DD/MM/YYYY" else "HH:MM",
                style = TextStyle(color = Color.Black, fontSize = 16.sp),
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp)
            )
            Image(
                painter = painterResource(icon),
                contentDescription = title,
            )
        }
    }
}

fun chooseTime(context: Context, onTimeSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    TimePickerDialog(
        context, { _, hourOfDay, minute ->
            val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
            onTimeSelected(selectedTime) // Update the Composable
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true
    ).show()
}

fun chooseDate(context: Context, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance() // Get current date

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selectedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
            onDateSelected(selectedDate) // Update the Composable
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // Restrict selection to only past and current dates
    datePickerDialog.datePicker.maxDate = calendar.timeInMillis

    datePickerDialog.show()
}

@Composable
fun CommonEditViewWithLabel(
    modifier: Modifier,
    title: String,
    text: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {

        Text(title, style = TextStyle(color = Color.Black, fontSize = 16.sp))
        TextField(
            value = text.toString(),
            onValueChange = { onValueChange(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Type here...") },
            textStyle = TextStyle(fontSize = 16.sp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent, // Transparent background
                focusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
            )
        )
    }
}

@Composable
fun CustomToolBar(modifier: Modifier) {
    Box(modifier.fillMaxWidth()) {
        Row(Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(R.drawable.ic_arrow_back),
                contentDescription = "Back Button",
                modifier = Modifier.clickable {

                })
            Spacer(modifier = Modifier.width(10.dp))
            Text("Add Expenses", style = TextStyle(color = Color.Black, fontSize = 16.sp))
        }
    }
}
