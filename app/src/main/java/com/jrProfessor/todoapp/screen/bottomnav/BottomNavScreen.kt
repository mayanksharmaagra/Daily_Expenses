package com.jrProfessor.todoapp.screen.bottomnav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import com.jrProfessor.todoapp.R

sealed class BottomNavScreen(
    val route: String,
    val title: String,
    val selectedIcon: Int,
    val unSelectedIcon: Int
) {
    object Dashboard : BottomNavScreen(
        route = "dashbaord",
        "Dashboard",
        selectedIcon = R.drawable.home_selected,
        unSelectedIcon = R.drawable.home_unselected
    )

    object Expenses : BottomNavScreen(
        route = "expenses",
        "Expenses",
        selectedIcon = R.drawable.expenses_selected,
        unSelectedIcon = R.drawable.expenses_unselected
    )

    object Analysis : BottomNavScreen(
        route = "analysis",
        "Analysis",
        selectedIcon = R.drawable.analytics_selected,
        unSelectedIcon = R.drawable.analytics_unselected
    )

    object Profile : BottomNavScreen(
        route = "profile",
        "Profile",
        selectedIcon = R.drawable.profile_selected,
        unSelectedIcon = R.drawable.profile_unselected
    )
}