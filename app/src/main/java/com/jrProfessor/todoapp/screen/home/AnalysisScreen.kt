package com.jrProfessor.todoapp.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.jrProfessor.todoapp.R
import com.jrProfessor.todoapp.model.CategoryWiseExpenses
import com.jrProfessor.todoapp.screen.CustomLoader
import com.jrProfessor.todoapp.screen.common.CustomToolBar
import com.jrProfessor.todoapp.ui.theme.CardColor
import com.jrProfessor.todoapp.ui.theme.PrimaryColor
import com.jrProfessor.todoapp.utils.AppUtils
import com.jrProfessor.todoapp.utils.AppUtils.pieChartColors
import com.jrProfessor.todoapp.viewmodel.HomeViewModel

@Composable
fun AnalysisScreen(viewModel: HomeViewModel? = null) {
    viewModel?.let {
        val categoryWiseExpenses by viewModel.categoryWiseExpenses.observeAsState(emptyList())
        val errorMessage by viewModel.errorMessage.observeAsState()
        val isLoading by viewModel.isLoading.observeAsState(false)

        LaunchedEffect(Unit) {
            viewModel.fetchExpensesByCategory()
        }
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
        ) {
            when {
                isLoading -> {
                    CustomLoader(true)
                }

                !errorMessage.isNullOrEmpty() -> {
                    Column(
                        modifier = Modifier.fillMaxSize(), // Fill the entire screen
                        verticalArrangement = Arrangement.Center, // Center vertically
                        horizontalAlignment = Alignment.CenterHorizontally // Center horizontally
                    ) {
                        Text(
                            text = errorMessage ?: "Something went wrong",
                            color = Color.Red,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                else -> {
                    val (categoryTitle, data, noDataFound) = createRefs()
                    if (categoryWiseExpenses.isNotEmpty()) {
                        CustomToolBar(
                            Modifier.constrainAs(categoryTitle) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            },
                            icon = painterResource(R.drawable.coins),
                            title = "Top Spending Categories",
                            textColor = PrimaryColor
                        )

                        CategoryWiseData(Modifier.constrainAs(data) {
                            top.linkTo(categoryTitle.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }, categoryWiseExpenses)
                    } else {
                        Text(text = "No Expenses Found",
                            fontSize = 30.sp,
                            color = Color.Red,
                            modifier = Modifier
                                .constrainAs(noDataFound) {
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    bottom.linkTo(parent.bottom)
                                }
                                .fillMaxSize()
                                .wrapContentSize(Alignment.Center))
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryWiseData(
    modifier: Modifier,
    expenseList: List<CategoryWiseExpenses>,
) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        LazyColumn(
            contentPadding = PaddingValues(6.dp), verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(expenseList) { expense ->
                ChildItem(expense)
            }
        }
    }
}

@Composable
fun ChildItem(expense: CategoryWiseExpenses? = null) {
    Card(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(), // Padding for the card
        shape = RoundedCornerShape(8.dp), // Rounded corners for the card
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = CardColor)
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(containerColor = pieChartColors[expense?.category]!!)
                ) {
                    Image(
                        modifier = Modifier
                            .size(30.dp)
                            .padding(8.dp),
                        painter = painterResource(AppUtils.getIconForCategory(expense?.category)),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    modifier = Modifier.weight(1f),
                    text = expense?.category ?: "Item Name",
                    color = pieChartColors[expense?.category]!!,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = AppUtils.getAmount(expense?.totalAmount) ?: "Price",
                    color = pieChartColors[expense?.category]!!,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Image(
                    modifier = Modifier
                        .size(20.dp)
                        .padding(2.dp),
                    painter = painterResource(R.drawable.ic_arrow_forward),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(pieChartColors[expense?.category]!!)
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewCallback(modifier: Modifier = Modifier) {
    AnalysisScreen()
}