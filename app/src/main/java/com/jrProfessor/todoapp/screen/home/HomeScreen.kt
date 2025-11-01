package com.jrProfessor.todoapp.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ContentAlpha
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jrProfessor.todoapp.R
import com.jrProfessor.todoapp.screen.bottomnav.BottomNavGraph
import com.jrProfessor.todoapp.screen.bottomnav.BottomNavScreen
import com.jrProfessor.todoapp.screen.common.ScreenClass
import com.jrProfessor.todoapp.ui.theme.PrimaryColor
import com.jrProfessor.todoapp.viewmodel.HomeViewModel

@Preview
@Composable
fun HomeScreenView(modifier: Modifier = Modifier) {
    HomeScreen()
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val currentBackStack = navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack.value?.destination?.route
    Scaffold(
        bottomBar = {
            // Hide the BottomNavBar for the third fragment
            if (currentDestination !in listOf(
                    ScreenClass.AddGoal.route,
                    ScreenClass.AddExpense.route
                )
            ) {
                BottomNavigationBar(navController)
            }
        },
        contentColor = MaterialTheme.colors.onSurface,
        floatingActionButton = {
            if (currentDestination == BottomNavScreen.Dashboard.route) {
                FloatingActionButton(
                    onClick = { navController.navigate(ScreenClass.AddExpense.route) },
                    backgroundColor = PrimaryColor
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End // FAB in center of bottom bar
    ) { innerPadding ->

        BottomNavGraph(navController, innerPadding)
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    var selectedPosition by remember {
        mutableIntStateOf(0)
    }
    val listScreen = listOf(
        BottomNavScreen.Dashboard,
        BottomNavScreen.Expenses,
        BottomNavScreen.Analysis,
        BottomNavScreen.Profile
    )
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        BottomNavigation(
            backgroundColor = Color.White,
        ) {
            listScreen.forEachIndexed { index, screen ->
                BottomNavigationItem(
                    label = {
                        Text(
                            text = screen.title,
                            style = TextStyle(fontSize = 12.sp),
                            color = if (selectedPosition == index) PrimaryColor else Color.Gray
                        )
                    },
                    icon = {
                        Icon(
                            painter = if (selectedPosition == index) {
                                painterResource(screen.selectedIcon)
                            } else {
                                painterResource(screen.unSelectedIcon)
                            },
                            contentDescription = "Navigation Icon",
                            modifier = Modifier.padding(5.dp),
                            tint = if (selectedPosition == index) PrimaryColor else Color.Gray
                        )
                    },
                    selected = currentDestination?.hierarchy?.any {
                        it.route == screen.route
                    } == true,
                    unselectedContentColor = LocalContentColor.current.copy(alpha = ContentAlpha.disabled),
                    onClick = {
                        selectedPosition = index
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id)
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}