package com.jrProfessor.todoapp.screen.bottomnav

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jrProfessor.todoapp.screen.common.ScreenClass
import com.jrProfessor.todoapp.screen.home.AnalysisScreen
import com.jrProfessor.todoapp.screen.home.AddExpenseScreen
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
            DashboardScreen(viewModel)
        }
        composable(route = BottomNavScreen.Expenses.route) {
            ExpenseScreen()
        }
        composable(route = BottomNavScreen.Analysis.route) {
            AnalysisScreen()
        }
        composable(route = BottomNavScreen.Profile.route) {
            ProfileScreen(viewModel, navController)
        }
        composable(route = ScreenClass.AddExpense.route) {
            AddExpenseScreen(viewModel)
        }
    }
}