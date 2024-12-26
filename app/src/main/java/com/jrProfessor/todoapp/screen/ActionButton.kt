package com.jrProfessor.todoapp.screen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jrProfessor.todoapp.ui.theme.PurpleGrey40

@Composable
fun ActionButton(
    modifier: Modifier = Modifier,
    colors: ButtonColors,
    title: String
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(percent = 50),
                spotColor = Color.Gray
            ),
        onClick = {

        },
        colors = colors
    ) {
        Text(title, fontStyle = FontStyle.Normal, fontSize = 20.sp)
    }
}