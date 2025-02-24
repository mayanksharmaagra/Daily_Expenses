package com.jrProfessor.todoapp.utils

import androidx.compose.ui.graphics.Color
import com.jrProfessor.todoapp.R
import com.jrProfessor.todoapp.model.IconSpinnerModel
import java.text.NumberFormat
import java.util.Locale
import java.util.UUID

object AppUtils {
    val DB_NAME = "ExpensesInfo"
    val EXPENSES_TABLE = "Expense_Table"
    val GOAL_TABLE = "Goal_Table"
    val USERS = "users"
    val pieChartColors = mapOf(
        "Saving" to Color(0xFF1F77B4), // Dark Blue
        "Investment" to Color(0xFF9A4C07), // Dark Orange
        "Debt Payments" to Color(0xFF167816), // Dark Green
        "Healthcare" to Color(0xFF930B0C), // Dark Red
        "Shopping" to Color(0xFF660FB6), // Dark Purple
        "Entertainment" to Color(0xFF0E5F9A), // Brown
        "Education" to Color(0xFF820962), // Pink
        "Fitness & Sports" to Color(0xFF444141), // Gray
        "Transportation" to Color(0xFF646506), // Olive Green
        "House" to Color(0xFF045057), // Teal
        "Food" to Color(0xFF1E2176), // Dark Navy
        "Travel" to Color(0xFF805807)  // Dark Gold
    )

    fun generateRandomId(): String {
        return UUID.randomUUID().toString().replace("-", "").take(32)
    }

    fun getIconForCategory(category: String?): Int {
        return when (category) {
            "Saving" -> {
                R.drawable.saving
            }

            "Investment" -> {
                R.drawable.investment
            }

            "Debt Payments" -> {
                R.drawable.debt
            }

            "Healthcare" -> {
                R.drawable.health
            }

            "Shopping" -> {
                R.drawable.shopping
            }

            "Entertainment" -> {
                R.drawable.entertainment
            }

            "Education" -> {
                R.drawable.education
            }

            "Fitness & Sports" -> {
                R.drawable.fitness
            }

            "Transportation" -> {
                R.drawable.transportation
            }

            "House" -> {
                R.drawable.house
            }

            "Food" -> {
                R.drawable.food
            }

            "Travel" -> {
                R.drawable.saving
            }

            else -> {
                0
            }
        }
    }

    fun getAmount(amount: String?): String? {
        return NumberFormat.getCurrencyInstance(Locale("en", "IN")).format(amount?.toDouble())
    }

    val CATEGORY = listOf(
        IconSpinnerModel(R.drawable.saving, "Saving", "Emergency Fund, Retirement, Fixed Deposits"),
        IconSpinnerModel(R.drawable.investment, "Investment", "Stocks, Mutual Funds, Crypto"),
        IconSpinnerModel(R.drawable.debt, "Debt Payments", "Loan EMI, Credit Card Payments"),
        IconSpinnerModel(R.drawable.health, "Healthcare", "Insurance, Medicines, Doctor Visits"),
        IconSpinnerModel(R.drawable.shopping, "Shopping", "Clothing, Electronics, Home Décor"),
        IconSpinnerModel(
            R.drawable.entertainment,
            "Entertainment",
            "Movies, Streaming Services, Concerts"
        ),
        IconSpinnerModel(R.drawable.education, "Education", "Tuition, Books, Courses"),
        IconSpinnerModel(
            R.drawable.fitness,
            "Fitness & Sports",
            "Gym Memberships, Sports Equipmen"
        ),
        IconSpinnerModel(
            R.drawable.transportation,
            "Transportation",
            "Fuel, Public Transport, Car Maintenance"
        ),
        IconSpinnerModel(R.drawable.house, "Housing", "Rent/Mortgage, Property Taxes, Utilities"),
        IconSpinnerModel(R.drawable.food, "Food", "Groceries, Dining Out, Snacks"),
        IconSpinnerModel(R.drawable.travel, "Travel", "Flights, Hotels, Sightseeing")
    )
}