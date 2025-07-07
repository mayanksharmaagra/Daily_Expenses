package com.jrProfessor.todoapp.screen.home

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.booleanResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.gson.Gson
import com.jrProfessor.todoapp.R
import com.jrProfessor.todoapp.intent.UserExpenseIntent
import com.jrProfessor.todoapp.model.ExpensesModel
import com.jrProfessor.todoapp.screen.CustomLoader
import com.jrProfessor.todoapp.screen.common.ScreenClass
import com.jrProfessor.todoapp.ui.theme.CardColor
import com.jrProfessor.todoapp.ui.theme.CardTextColor
import com.jrProfessor.todoapp.ui.theme.PrimaryColor
import com.jrProfessor.todoapp.utils.AppUtils
import com.jrProfessor.todoapp.viewmodel.HomeViewModel

@Preview
@Composable
fun Preview(modifier: Modifier = Modifier) {
}

@Composable
fun ExpenseScreen(viewModel: HomeViewModel = hiltViewModel(), navController: NavHostController) {
    viewModel?.let {
        val expensesState by viewModel.expensesState.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.performUserExpenseIntent(UserExpenseIntent.GetAllExpenses)
        }

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
        ) {
            when {
                expensesState.loading -> {
                    CustomLoader(true)
                }

                expensesState.success.isNullOrEmpty() == false -> {
                    var totalAmount = 0.0
                    expensesState.result?.forEach {
                        totalAmount += it.amount.toDouble()
                    }
                    val (header, categoryItem, noDataFound) = createRefs()
                    HeaderView(modifier = Modifier.constrainAs(header) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }, totalAmount)
                    if (expensesState.result?.isNotEmpty() == true) {
                        CategoryDisplayView(Modifier.constrainAs(categoryItem) {
                            top.linkTo(header.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }, expensesState.result!!, deleteItem = {
                            if (!it.isNullOrEmpty())
                                viewModel.performUserExpenseIntent(
                                    UserExpenseIntent.DeleteCategory,
                                    id = it
                                )
                        }, navigateToEditExpenses = { id, _expense ->
                            navController?.navigate(
                                ScreenClass.AddExpense.route + "?id=$id&expense=${
                                    Uri.encode(
                                        Gson().toJson(
                                            _expense
                                        )
                                    )
                                }"
                            )
                        })
                    } else {
                        Text(
                            text = "No Expenses Found",
                            fontSize = 30.sp,
                            color = Color.Red,
                            modifier = Modifier
                                .constrainAs(noDataFound) {
                                    top.linkTo(header.bottom)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    bottom.linkTo(parent.bottom)
                                }
                                .fillMaxSize()
                                .wrapContentSize(Alignment.Center)
                        )
                    }
                }

                expensesState.error.isNullOrEmpty() == false -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(), // Fill the entire screen
                        verticalArrangement = Arrangement.Center, // Center vertically
                        horizontalAlignment = Alignment.CenterHorizontally // Center horizontally
                    ) {
                        Text(
                            text = expensesState.error ?: "Something went wrong",
                            color = Color.Red,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryDisplayView(
    modifier: Modifier,
    expenseList: List<ExpensesModel>,
    deleteItem: (String?) -> Unit,
    navigateToEditExpenses: (String?, ExpensesModel?) -> Unit
) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        LazyColumn(
            contentPadding = PaddingValues(6.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(expenseList) { expense ->
                CategoryItem(expense, deleteItem = {
                    deleteItem(expense.id)
                }, navigateToEditExpenses = { id, _expense ->
                    navigateToEditExpenses(id, _expense)
                }
                )
            }
        }
    }
}

@Composable
fun CategoryItem(
    expense: ExpensesModel? = null,
    deleteItem: (String?) -> Unit,
    navigateToEditExpenses: (String?, ExpensesModel?) -> Unit
) {
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
                Text(
                    modifier = Modifier.weight(1f),
                    text = expense?.itemName ?: "Item Name",
                    color = AppUtils.pieChartColors[expense?.category] ?: CardTextColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = AppUtils.getAmount(expense?.amount?.toDouble()) ?: "Item Price",
                    color = AppUtils.pieChartColors[expense?.category] ?: CardTextColor,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Image(
                    modifier = Modifier
                        .size(10.dp)
                        .clickable {
                            navigateToEditExpenses(expense?.id, expense)
                        },
                    painter = painterResource(R.drawable.ic_edit),
                    contentDescription = "Edit Icon",
                    colorFilter = ColorFilter.tint(
                        AppUtils.pieChartColors[expense?.category] ?: CardTextColor
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Image(
                    modifier = Modifier.clickable {
                        deleteItem(expense?.id)
                    },
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = "Close Icon",
                    colorFilter = ColorFilter.tint(
                        AppUtils.pieChartColors[expense?.category] ?: CardTextColor
                    )
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Row(
                    modifier = Modifier
                        .background(
                            color = AppUtils.pieChartColors[expense?.category] ?: CardTextColor,
                            shape = RoundedCornerShape(15.dp) // Rounded corners
                        )
                        .padding(8.dp, 0.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(AppUtils.getIconForCategory(expense?.category)),
                        tint = Color.White,
                        contentDescription = "Icon",
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = expense?.category ?: "Category Name",
                        color = Color.White,
                        fontSize = 10.sp
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    painter = painterResource(R.drawable.ic_calendar),
                    tint = AppUtils.pieChartColors[expense?.category] ?: CardTextColor,
                    contentDescription = "Calendar Icon", modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = expense?.dateValue ?: "Item Date",
                    color = AppUtils.pieChartColors[expense?.category] ?: CardTextColor,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun HeaderView(modifier: Modifier, totalAmount: Double) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp)
            .background(color = PrimaryColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Total Expenses", fontSize = 18.sp, color = Color.White)
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = AppUtils.getAmount(totalAmount) ?: "Total Amount",
                fontSize = 30.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}
