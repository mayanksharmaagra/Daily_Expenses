package com.jrProfessor.todoapp.screen.bottomnav

import android.net.Uri
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.jrProfessor.todoapp.model.ExpensesModel
import com.jrProfessor.todoapp.screen.common.ScreenClass
import com.jrProfessor.todoapp.screen.home.AnalysisScreen
import com.jrProfessor.todoapp.screen.home.AddExpenseScreen
import com.jrProfessor.todoapp.screen.home.AddGoalScreen
import com.jrProfessor.todoapp.screen.home.DashboardScreen
import com.jrProfessor.todoapp.screen.home.ExpenseScreen
import com.jrProfessor.todoapp.screen.home.ProfileScreen
import com.jrProfessor.todoapp.viewmodel.HomeViewModel

@Composable
fun BottomNavGraph(
    navController: NavHostController,
    innerPadding: PaddingValues,
    viewModel: HomeViewModel?
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavScreen.Dashboard.route,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(route = BottomNavScreen.Dashboard.route) {
            DashboardScreen(viewModel,navController)
        }
        composable(route = BottomNavScreen.Expenses.route) {
            ExpenseScreen(viewModel, navController)
        }
        composable(route = BottomNavScreen.Analysis.route) {
            AnalysisScreen(viewModel)
        }
        composable(route = BottomNavScreen.Profile.route) {
            ProfileScreen(viewModel, navController)
        }
        composable(route = ScreenClass.AddGoal.route) {
            AddGoalScreen(viewModel, navController)
        }
        composable(
            route = ScreenClass.AddExpense.route + "?id={id}&expense={expense}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                    defaultValue = null
                    nullable = true
                },navArgument("expense") {
                    type = NavType.StringType
                    defaultValue = null
                    nullable = true
                },)
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id");
            val expenseJson = backStackEntry.arguments?.getString("expense");

            val decodedExpenseJson = expenseJson?.let { Uri.decode(it) } // Decode JSON
            val expenseModel = Gson().fromJson(decodedExpenseJson, ExpensesModel::class.java)
            AddExpenseScreen(viewModel, id, expenseModel, navController)
        }
    }
}