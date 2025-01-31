package com.jrProfessor.todoapp.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jrProfessor.todoapp.ui.theme.PrimaryColor

@Composable
fun CustomLoader(
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    if (isLoading) {
        Box(
            modifier = modifier
                .fillMaxSize() // Fill the screen
                .background(
                    Color.Black.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(12.dp)
                ), // Optional background overlay
            contentAlignment = Alignment.Center
        ) {
            // CardView container for the ProgressBar
            Card(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(24.dp), // Padding for the card
                shape = RoundedCornerShape(12.dp), // Rounded corners for the card
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                // Loading content inside the Card
                Column(
                    modifier = Modifier
                        .padding(20.dp), // Padding inside the card
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(50.dp), // Customize the size of the ProgressBar
                        color = PrimaryColor // Customize the color of the ProgressBar
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Loading...",
                        style = TextStyle(color = Color.Black, fontSize = 16.sp)
                    )
                }
            }
        }
    }
}
