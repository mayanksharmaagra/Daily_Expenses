package com.jrProfessor.todoapp.screen.common

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Calendar

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