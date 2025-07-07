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
import com.jrProfessor.todoapp.screen.home.AddWalletScreen
import com.jrProfessor.todoapp.screen.home.DashboardScreen
import com.jrProfessor.todoapp.screen.home.ExpenseScreen
import com.jrProfessor.todoapp.screen.home.ProfileScreen

@Composable
fun BottomNavGraph(
    navController: NavHostController,
    innerPadding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavScreen.Dashboard.route,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(route = BottomNavScreen.Dashboard.route) {
            DashboardScreen(navHostController = navController)
        }
        composable(route = BottomNavScreen.Expenses.route) {
            ExpenseScreen(navController = navController)
        }
        composable(route = BottomNavScreen.Analysis.route) {
            AnalysisScreen(navController = navController)
        }
        composable(route = BottomNavScreen.Profile.route) {
            ProfileScreen(navController = navController)
        }
        composable(route = ScreenClass.AddGoal.route) {
            AddGoalScreen(navController = navController)
        }
        composable(
            route = ScreenClass.AddExpense.route + "?id={id}&expense={expense}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                    defaultValue = null
                    nullable = true
                },
                navArgument("expense") {
                    type = NavType.StringType
                    defaultValue = null
                    nullable = true
                },
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id");
            val expenseJson = backStackEntry.arguments?.getString("expense");

            val decodedExpenseJson = expenseJson?.let { Uri.decode(it) } // Decode JSON
            val expenseModel = Gson().fromJson(decodedExpenseJson, ExpensesModel::class.java)
            AddExpenseScreen(id = id, expenseModel = expenseModel, navController =  navController)
        }
        composable(route = ScreenClass.AddWallet.route) {
            AddWalletScreen(navController = navController)
        }
    }
}