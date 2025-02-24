package com.jrProfessor.todoapp.screen.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.jrProfessor.todoapp.model.CategoryWiseExpenses
import com.jrProfessor.todoapp.utils.AppUtils.pieChartColors

@Composable
fun ExpensePieChart(expensesByCategory: List<CategoryWiseExpenses>) {
    val totalAmount = expensesByCategory.sumOf { it.totalAmount }
    val pieSize = 200.dp // Default size for the pie chart
    val density = LocalDensity.current
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Canvas(
            modifier = Modifier
                .size(pieSize)
                .padding(16.dp)
        ) {
            var startAngle = 0f

            expensesByCategory.forEachIndexed { index, categoryData ->
                val sweepAngle = (categoryData.totalAmount / totalAmount * 360).toFloat()
                val colorBg = pieChartColors[categoryData.category]
                drawArc(
                    color = colorBg!!,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true
                )
                // Calculate position for text inside the slice
                val midAngle = startAngle + (sweepAngle / 2)
                val radius = size.minDimension / 3
                val textX = center.x + (radius * kotlin.math.cos(Math.toRadians(midAngle.toDouble()))).toFloat()
                val textY = center.y + (radius * kotlin.math.sin(Math.toRadians(midAngle.toDouble()))).toFloat()

                // Dynamically adjust text size based on pie chart size
                val textSizePx = with(density) { (size.minDimension * 0.03f).dp.toPx() }
                val percentage = String.format("%.2f", (categoryData.totalAmount / totalAmount * 100)) + "%"

                // Draw White Text inside Pie Chart
                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        percentage,
                        textX,
                        textY,
                        android.graphics.Paint().apply {
                            color = android.graphics.Color.WHITE
                            textSize = textSizePx
                            textAlign = android.graphics.Paint.Align.CENTER
                            isFakeBoldText = true
                        }
                    )
                }

                startAngle += sweepAngle
            }
        }

    }
}