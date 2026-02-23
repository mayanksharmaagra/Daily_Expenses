package com.jrProfessor.todoapp.utils

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomToast(message: String, iconRes: Int, durationMillis: Long = 2000, onDismiss: () -> Unit) {
    var isVisible by remember { mutableStateOf(true) }

    // Hide the toast after the specified duration
    LaunchedEffect(key1 = Unit) {
        Handler(Looper.getMainLooper()).postDelayed({
            isVisible = false
            onDismiss()
        }, durationMillis)
    }

    if (isVisible) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 30.dp), // Adjust for positioning
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .background(Color.Gray, shape = RoundedCornerShape(10.dp))
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .wrapContentSize()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = "Error Icon",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = message,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
