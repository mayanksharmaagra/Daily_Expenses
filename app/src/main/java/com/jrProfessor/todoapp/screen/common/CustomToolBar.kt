package com.jrProfessor.todoapp.screen.common

import android.icu.text.CaseMap.Title
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.jrProfessor.todoapp.R

@Composable
fun CustomToolBar(
    modifier: Modifier,
    navController: NavHostController,
    icon: Painter,
    title: String,
    textColor: Color
) {
    Box(modifier.fillMaxWidth()) {
        Row(Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = icon,
                contentDescription = "Back Button",
                modifier = Modifier.clickable {
                    navController.popBackStack()
                },
                tint = textColor,
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = title,
                style = TextStyle(color = textColor, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            )
        }
    }
}