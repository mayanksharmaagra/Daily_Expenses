package com.jrProfessor.todoapp.repository

import android.content.SharedPreferences
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.jrProfessor.todoapp.model.CategoryWiseExpenses
import com.jrProfessor.todoapp.model.ExpensesModel
import com.jrProfessor.todoapp.model.GoalsModel
import com.jrProfessor.todoapp.model.User
import com.jrProfessor.todoapp.utils.AppUtils.DB_NAME
import com.jrProfessor.todoapp.utils.AppUtils.EXPENSES_TABLE
import com.jrProfessor.todoapp.utils.AppUtils.GOAL_TABLE
import com.jrProfessor.todoapp.utils.AppUtils.USERS
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase,
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) {
    companion object {
        var IS_LOGGED = "is_logged"
        var USER = "user"
    }

    fun signInAccount(
        user: User,
        onSuccess: (User?, String?) -> Unit,
        onError: (User?, String?) -> Unit
    ) {
        val databaseReference = firebaseDatabase.getReference(DB_NAME)
        firebaseAuth.signInWithEmailAndPassword(user.emailId, user.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Fetch user data from the database
                    if (firebaseAuth.currentUser != null && !firebaseAuth.currentUser?.uid.isNullOrEmpty()) {
                        val uid = firebaseAuth.currentUser?.uid!!
                        databaseReference.child(USERS).child(uid)
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val user = snapshot.getValue(User::class.java)
                                    if (user != null) {
                                        saveUser(user)
                                        onSuccess(user, null)
                                    } else {
                                        onError(null, "User data not found")
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    onError(null, error.message)
                                }
                            })
                    } else {
                        onError(null, "User not exist...")
                    }
                } else {
                    onError(null, "Database Error: ${task.exception?.message}")
                }
            }
    }

    private fun saveUser(user: User) {

        val userJson = gson.toJson(user)
        sharedPreferences.edit()
            .putString(USER, userJson)
            .putBoolean(IS_LOGGED, true)
            .apply()
    }

    fun signUpAccount(user: User, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(user.emailId, user.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = firebaseAuth.currentUser?.uid ?: return@addOnCompleteListener
                    saveUserAuthentication(uid, user, onSuccess, onError)
                } else {
                    onError("Database Error: ${task.exception?.message}")
                }
            }
    }

    private fun saveUserAuthentication(
        uid: String,
        user: User,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        val databaseReference = firebaseDatabase.getReference(DB_NAME)

        databaseReference.child(USERS).child(uid).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    databaseReference.child(USERS).child(uid).setValue(user)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                onSuccess("Your information saved successfully")
                            } else {
                                onError("Database Error: ${task.exception?.message}")
                            }
                        }
                } else {
                    onError("Email Id already exist.")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                onError("Database Error: " + error.message)
            }
        })
    }

    fun isLoggedIn() = sharedPreferences.getBoolean(IS_LOGGED, false)
    fun getUser(): User? {
        val userJson = sharedPreferences.getString(USER, null)
        return if (userJson != null) {
            gson.fromJson(userJson, User::class.java)
        } else {
            null
        }
    }

    fun logout() {
        firebaseAuth.signOut()
        with(sharedPreferences.edit()) {
            clear()
            apply()
        }
    }


    fun saveExpenses(
        expenses: HashMap<String, String>,
        onSuccess: (Boolean) -> Unit,
        onError: (Boolean, String) -> Unit
    ) {
        val uid = firebaseAuth.currentUser?.uid!!
        val databaseReference = firebaseDatabase.getReference(DB_NAME)
        val expensesRef = databaseReference.child(EXPENSES_TABLE).child(uid).push()
        val expensesId = expensesRef.key
        if (expensesId != null) {
            expenses["id"] = expensesId
            expensesRef.setValue(expenses)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onSuccess(true)

                    } else {
                        onError(false, "Database Error: ${task.exception?.message}")
                    }
                }
        } else {
            onError(false, "Error generating unique ID")
        }
    }

    fun getAllExpenses(
        onSuccess: (List<ExpensesModel>) -> Unit,
        onError: (Boolean, String) -> Unit
    ) {
        val uid = firebaseAuth.currentUser?.uid ?: return onError(false, "User not authenticated")

        val databaseReference = firebaseDatabase.getReference(DB_NAME)
        databaseReference.child(EXPENSES_TABLE).child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val expensesList = mutableListOf<ExpensesModel>()

                    for (expenseSnapshot in snapshot.children) {
                        val expense = expenseSnapshot.getValue(ExpensesModel::class.java)
                        if (expense != null) {
                            expensesList.add(expense)
                        }
                    }

                    onSuccess(expensesList)
                }

                override fun onCancelled(error: DatabaseError) {
                    onError(false, "Database Error: ${error.message}")
                }
            })
    }

    fun deleteCategory(
        expensesId: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        val uid = firebaseAuth.currentUser?.uid ?: return onError("User not authenticated")
        val databaseReference = firebaseDatabase.getReference(DB_NAME)
        databaseReference.child(EXPENSES_TABLE).child(uid).child(expensesId).removeValue()
            .addOnCompleteListener {
                onSuccess("Successfully Remove")
            }.addOnFailureListener { error ->
                onError("Failed to delete ${error.message}")
            }
    }

    fun updateExpenses(
        expensesId: String,
        model: ExpensesModel,
        onSuccess: (String) -> Unit,
        onError: (Boolean, String) -> Unit
    ) {
        val uid = firebaseAuth.currentUser?.uid ?: return onError(false, "User not authenticated")
        val databaseReference = firebaseDatabase.getReference(DB_NAME)
        databaseReference.child(EXPENSES_TABLE)
            .child(uid)
            .child(expensesId)
            .setValue(model)
            .addOnCompleteListener {
                onSuccess("Successfully Updated Data")
            }.addOnFailureListener { error ->
                onError(false, "Failed to update ${error.message}")
            }
    }

    fun fetchExpensesByCategory(
        onResult: (List<CategoryWiseExpenses>) -> Unit,
        onError: (Boolean, String) -> Unit
    ) {
        val uid = firebaseAuth.currentUser?.uid ?: return onError(false, "User not authenticated")
        val databaseReference =
            firebaseDatabase.getReference(DB_NAME).child(EXPENSES_TABLE).child(uid)
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val categoryWiseExpenses = mutableMapOf<String, MutableList<ExpensesModel>>()
                val categoryTotalAmount = mutableMapOf<String, Double>()
                for (expenseSnapshot in snapshot.children) { // Loop through all expenses
                    val expense = expenseSnapshot.getValue(ExpensesModel::class.java)
                    expense?.let {
                        val category = it.category
                        val amount = it.amount.toDoubleOrNull() ?: 0.0

                        categoryWiseExpenses.getOrPut(it.category) { mutableListOf() }.add(it)
                        // Calculate total amount for each category
                        categoryTotalAmount[category] =
                            categoryTotalAmount.getOrDefault(category, 0.0) + amount
                    }
                }

                val finalResult = categoryWiseExpenses.map { (category, expenses) ->
                    CategoryWiseExpenses(
                        category = category,
                        expenses = expenses,
                        totalAmount = categoryTotalAmount[category] ?: 0.0
                    )
                }

                onResult(finalResult)
            }

            override fun onCancelled(error: DatabaseError) {
                onError(false, "Database Error: ${error.message}")
            }

        })
    }

    fun saveGoal(
        goal: HashMap<String, String>,
        onSuccess: (Boolean) -> Unit,
        onError: (Boolean, String) -> Unit
    ) {
        val uid = firebaseAuth.currentUser?.uid ?: return onError(false, "User not authenticated")
        val databaseReference = firebaseDatabase.getReference(DB_NAME).child(GOAL_TABLE)
        val goalRef = databaseReference.child(uid).push()
        val goalId = goalRef.key
        if (goalId != null) {
            goal["id"] = goalId
            goalRef.setValue(goal)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onSuccess(true)
                    } else {
                        onError(false, "Database Error: ${task.exception?.message}")
                    }
                }
        } else {
            onError(false, "Error generating unique ID")
        }
    }

    fun getAllGoals(
        onSuccess: (List<GoalsModel>) -> Unit,
        onError: (Boolean, String) -> Unit
    ) {
        val uid = firebaseAuth.currentUser?.uid ?: return onError(false, "User not authenticated")

        val databaseReference = firebaseDatabase.getReference(DB_NAME)
        databaseReference.child(GOAL_TABLE).child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val goalList = mutableListOf<GoalsModel>()

                    for (expenseSnapshot in snapshot.children) {
                        val expense = expenseSnapshot.getValue(GoalsModel::class.java)
                        if (expense != null) {
                            goalList.add(expense)
                        }
                    }
                    val jsonData = Gson().toJson(goalList)
                    Log.d("FirebaseData", "Category-Wise Expenses: $jsonData")

                    onSuccess(goalList)
                }

                override fun onCancelled(error: DatabaseError) {
                    onError(false, "Database Error: ${error.message}")
                }
            })
    }

    fun deleteGoal(
        goalId: String?,
        onSuccess: (String) -> Unit,
        onError: (Boolean, String) -> Unit
    ) {
        val uid = firebaseAuth.currentUser?.uid ?: return onError(false, "User not authenticated")
        val databaseReference = firebaseDatabase.getReference(DB_NAME)
        goalId?.let {
            databaseReference.child(GOAL_TABLE).child(uid).child(it).removeValue()
                .addOnCompleteListener {
                    onSuccess("Successfully Remove")
                }.addOnFailureListener { error ->
                    onError(false, "Failed to delete ${error.message}")
                }
        }
    }
}