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
import com.jrProfessor.todoapp.utils.AppUtils.ADD_AMOUNT
import com.jrProfessor.todoapp.utils.AppUtils.DB_NAME
import com.jrProfessor.todoapp.utils.AppUtils.EXPENSES_TABLE
import com.jrProfessor.todoapp.utils.AppUtils.GOAL_TABLE
import com.jrProfessor.todoapp.utils.AppUtils.USERS
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase,
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) : UserRepository {
    companion object {
        var IS_LOGGED = "is_logged"
        var USER = "user"
    }

    override fun signInAccount(user: User): Flow<Result<User>> = callbackFlow {
        val databaseReference = firebaseDatabase.getReference(DB_NAME)
        val authTask = firebaseAuth.signInWithEmailAndPassword(user.emailId, user.password)
        var listener: ValueEventListener? = null

        authTask
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = firebaseAuth.currentUser
                    val uid = firebaseUser?.uid

                    if (uid.isNullOrEmpty()) {
                        trySend(Result.failure(Exception("User not exist...")))
                        close()
                        return@addOnCompleteListener
                    }
                    listener = object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val user = snapshot.getValue(User::class.java)
                            if (user != null) {
                                saveUser(user)
                                trySend(Result.success(user))
                                close()
                            } else {
                                trySend(Result.failure(Exception("User data not found")))
                                close()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            trySend(Result.failure(Exception(error.message)))
                            close()
                        }
                    }
                    databaseReference.child(USERS).child(uid)
                        .addListenerForSingleValueEvent(listener)
                } else {
                    trySend(Result.failure(task.exception ?: Exception("Login failed")))
                    close()
                }
            }
            .addOnFailureListener { error ->
                trySend(Result.failure(Exception("Failed to sign-in ${error.message}")))
                close()
            }
        awaitClose {
            listener?.let {
                val uid = firebaseAuth.currentUser?.uid
                if (!uid.isNullOrEmpty()) {
                    databaseReference.child(USERS).child(uid).removeEventListener(it)
                }
            }
        }
    }

    override fun saveUser(user: User) {
        val userJson = gson.toJson(user)
        sharedPreferences.edit().putString(USER, userJson).putBoolean(IS_LOGGED, true).apply()
    }

    override fun signUpAccount(user: User): Flow<Result<String>> = callbackFlow {
        val databaseReference = firebaseDatabase.getReference(DB_NAME).child(USERS)
        var listener: ValueEventListener? = null
        firebaseAuth.createUserWithEmailAndPassword(user.emailId, user.password)
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    trySend(Result.failure(Exception("Auth Error: ${task.exception?.message}")))
                    close()
                    return@addOnCompleteListener
                }
                val uid = firebaseAuth.currentUser?.uid
                if (uid.isNullOrEmpty()) {
                    trySend(Result.failure(Exception("User UID is null")))
                    close()
                    return@addOnCompleteListener
                }
                listener = object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            trySend(Result.failure(Exception("Email ID already exists.")))
                            close()
                            return
                        }
                        databaseReference.child(uid).setValue(user).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                trySend(Result.success("Your information saved successfully"))
                                close()
                            } else {
                                trySend(Result.failure(Exception("Database Error: ${task.exception?.message}")))
                                close()
                            }
                        }.addOnFailureListener { error ->
                            trySend(Result.failure(Exception("Failed to signup ${error.message}")))
                            close()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        trySend(Result.failure(Exception("Database Error: " + error.message)))
                        close()
                    }
                }

                databaseReference.child(uid).addListenerForSingleValueEvent(listener)
            }
            .addOnFailureListener { error ->
                trySend(Result.failure(Exception("Failed to create user ${error.message}")))
                close()
            }
        awaitClose {
            listener?.let {
                val uid = firebaseAuth.currentUser?.uid
                if (uid != null) {
                    databaseReference.child(uid).removeEventListener(listener)
                }
            }
        }
    }

    override fun isLoggedIn() = sharedPreferences.getBoolean(IS_LOGGED, false)

    override fun getUser(): User? {
        val userJson = sharedPreferences.getString(USER, null)
        return if (userJson != null) {
            gson.fromJson(userJson, User::class.java)
        } else {
            null
        }
    }

    override fun getAllGoals(): Flow<Result<List<GoalsModel>>> = callbackFlow {
        val uid = firebaseAuth.currentUser?.uid
        if (uid.isNullOrEmpty()) {
            trySend(Result.failure(Exception("User not authenticated")))
            close()
            return@callbackFlow
        }
        val databaseReference = firebaseDatabase.getReference(DB_NAME).child(GOAL_TABLE).child(uid)

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val goals = snapshot.children.mapNotNull {
                    it.getValue(GoalsModel::class.java)
                }
                Log.e("TAG", "onDataChange: " + goals)
                trySend(Result.success(goals))
                close()
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Result.failure(Exception("Database Error: ${error.message}")))
                close()
            }
        }
        databaseReference.addListenerForSingleValueEvent(listener)
        awaitClose {
            databaseReference.removeEventListener(listener)
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
        with(sharedPreferences.edit()) {
            clear()
            apply()
        }
    }

    override fun saveExpenses(expenses: ExpensesModel): Flow<Result<String>> = callbackFlow {
        val uid = firebaseAuth.currentUser?.uid
        if (uid.isNullOrEmpty()) {
            trySend(Result.failure(Exception("User not authenticated")))
            close()
            return@callbackFlow
        }
        val databaseReference =
            firebaseDatabase.getReference(DB_NAME).child(EXPENSES_TABLE).child(uid)
        val expensesRef = databaseReference.push()
        val expensesId = expensesRef.key
        if (expensesId == null) {
            trySend(Result.failure(Exception("Error generating unique ID")))
            close()
            return@callbackFlow
        }
        expenses.id = expensesId
        expensesRef.setValue(expenses).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                trySend(Result.success("Successfully save record"))
                close()
            } else {
                trySend(Result.failure(Exception("Database Error: ${task.exception?.message}")))
                close()
            }
        }.addOnFailureListener { error ->
            trySend(Result.failure(Exception("Failed to save expenses ${error.message}")))
            close()
        }
        // Mandatory cleanup block for callbackFlow
        awaitClose {
            // No listeners to remove, but still required to prevent warnings/leaks
        }
    }

    override fun getAllExpenses(): Flow<Result<List<ExpensesModel>>> = callbackFlow {
        val uid = firebaseAuth.currentUser?.uid
        if (uid.isNullOrEmpty()) {
            trySend(Result.failure(Exception("User not authenticated")))
            close()
            return@callbackFlow
        }

        val databaseReference =
            firebaseDatabase.getReference(DB_NAME).child(EXPENSES_TABLE).child(uid)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val expensesList = snapshot.children.mapNotNull {
                    it.getValue(ExpensesModel::class.java)
                }
                trySend(Result.success(expensesList))
                close()
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Result.failure(Exception("Database Error: ${error.message}")))
                close()
            }
        }
        databaseReference.addListenerForSingleValueEvent(listener)
        awaitClose {
            databaseReference.removeEventListener(listener)
        }
    }

    override fun deleteCategory(expensesId: String): Flow<Result<String>> = callbackFlow {
        val uid = firebaseAuth.currentUser?.uid
        if (uid.isNullOrEmpty()) {
            trySend(Result.failure(Exception("User not authenticated")))
            close()
            return@callbackFlow
        }
        val databaseReference =
            firebaseDatabase.getReference(DB_NAME).child(EXPENSES_TABLE).child(uid)
        databaseReference.child(expensesId).removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                trySend(Result.success("Successfully Remove"))
                close()
            } else {
                trySend(Result.failure(Exception("Database Error: ${task.exception?.message}")))
                close()
            }
        }.addOnFailureListener { error ->
            trySend(Result.failure(Exception("Failed to delete ${error.message}")))
            close()
        }
        // Mandatory cleanup block for callbackFlow
        awaitClose {
            // No listeners to remove, but still required to prevent warnings/leaks
        }
    }

    override fun updateExpenses(expensesId: String, model: ExpensesModel): Flow<Result<String>> =
        callbackFlow {
            val uid = firebaseAuth.currentUser?.uid
            if (uid.isNullOrEmpty()) {
                trySend(Result.failure(Exception("User not authenticated")))
                close()
                return@callbackFlow
            }
            val databaseReference =
                firebaseDatabase.getReference(DB_NAME).child(EXPENSES_TABLE).child(uid)
                    .child(expensesId)
            databaseReference.setValue(model).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(Result.success("Successfully update record"))
                    close()
                } else {
                    trySend(Result.failure(Exception("Database Error: ${task.exception?.message}")))
                    close()
                }
            }.addOnFailureListener { error ->
                trySend(Result.failure(Exception("Failed to update ${error.message}")))
                close()
            }
            // Mandatory cleanup block for callbackFlow
            awaitClose {
                // No listeners to remove, but still required to prevent warnings/leaks
            }
        }

    override fun fetchExpensesByCategory(): Flow<Result<List<CategoryWiseExpenses>>> =
        callbackFlow {
            val uid = firebaseAuth.currentUser?.uid
            if (uid.isNullOrEmpty()) {
                trySend(Result.failure(Exception("User not authenticated")))
                close()
                return@callbackFlow
            }

            val databaseReference =
                firebaseDatabase.getReference(DB_NAME).child(EXPENSES_TABLE).child(uid)
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val categoryMap = mutableMapOf<String, MutableList<ExpensesModel>>()
                    val categoryTotalAmount = mutableMapOf<String, String>()
                    snapshot.children.mapNotNull { it.getValue(ExpensesModel::class.java) }
                        .forEach { expense ->
                            val category = expense.category
                            val amount = expense.amount
                            categoryMap.getOrPut(category) { mutableListOf() }.add(expense)
                            // Calculate total amount for each category
                            categoryTotalAmount[category] =
                                (categoryTotalAmount.getOrDefault(category, 0.0).toString()
                                    .toDouble() + amount.toDouble()).toString()
                        }

                    val finalResult = categoryMap.map { (category, expenses) ->
                        CategoryWiseExpenses(
                            category = category,
                            expenses = expenses,
                            totalAmount = categoryTotalAmount[category] ?: "0.0"
                        )
                    }
                    Log.e("TAG", "onDataChange: category fetch " + finalResult)
                    trySend(Result.success(finalResult))
                    close()
                }

                override fun onCancelled(error: DatabaseError) {
                    trySend(Result.failure(Exception("Database Error: ${error.message}")))
                    close()
                }
            }
            databaseReference.addListenerForSingleValueEvent(listener)
            awaitClose {
                databaseReference.removeEventListener(listener)
            }
        }

    override fun saveGoal(goal: HashMap<String, Any>): Flow<Result<String>> = callbackFlow {
        val uid = firebaseAuth.currentUser?.uid
        if (uid.isNullOrEmpty()) {
            trySend(Result.failure(Exception("User not authenticated")))
            close()
            return@callbackFlow
        }
        val databaseReference = firebaseDatabase.getReference(DB_NAME).child(GOAL_TABLE).child(uid)
        val goalRef = databaseReference.push()
        val goalId = goalRef.key
        if (goalId != null) {
            goal["id"] = goalId
            goalRef.setValue(goal).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(Result.success("Successfully save goal"))
                    close()
                } else {
                    trySend(Result.failure(Exception("Database Error: ${task.exception?.message}")))
                    close()
                }
            }.addOnFailureListener { error ->
                trySend(Result.failure(Exception("Failed to save ${error.message}")))
                close()
            }
        } else {
            trySend(Result.failure(Exception("Error generating unique ID")))
            close()
        }
        // Mandatory cleanup block for callbackFlow
        awaitClose {
            // No listeners to remove, but still required to prevent warnings/leaks
        }
    }

    override fun deleteGoal(
        goalId: String?
    ): Flow<Result<String>> = callbackFlow {
        val uid = firebaseAuth.currentUser?.uid
        if (uid.isNullOrEmpty()) {
            trySend(Result.failure(Exception("User not authenticated")))
            close()
            return@callbackFlow
        }
        val databaseReference = firebaseDatabase.getReference(DB_NAME).child(GOAL_TABLE).child(uid)
        goalId?.let {
            databaseReference.child(it).removeValue().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(Result.success("Successfully Remove"))
                    close()
                } else {
                    trySend(Result.failure(Exception("Database Error: ${task.exception?.message}")))
                    close()
                }
            }.addOnFailureListener { error ->
                trySend(Result.failure(Exception("Failed to delete ${error.message}")))
                close()
            }
        }
        // Mandatory cleanup block for callbackFlow
        awaitClose {
            // No listeners to remove, but still required to prevent warnings/leaks
        }
    }

    override fun saveWalletForGoal(
        amount: String, goalId: String
    ): Flow<Result<String>> = callbackFlow {
        val uid = firebaseAuth.currentUser?.uid
        if (uid.isNullOrEmpty()) {
            trySend(Result.failure(Exception("User not authenticated")))
            close()
            return@callbackFlow
        }
        val databaseReference = firebaseDatabase.getReference(DB_NAME).child(GOAL_TABLE).child(uid)
        val goalRef = databaseReference.child(goalId)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val oldAmount = snapshot.getValue(String::class.java)
                Log.i("TAG", "saveWalletForGoal:==== $oldAmount")
                val newAddAmount = (oldAmount?.toDouble()!! + amount.toDouble()).toString()
                goalRef
                    .child(ADD_AMOUNT)
                    .setValue(newAddAmount)
                    .addOnSuccessListener {
                        Log.i("TAG", "saveWalletForGoal:=== $newAddAmount")
                        trySend(Result.success("Wallet updated successfully"))
                        close()
                    }.addOnFailureListener { error ->
                        Log.i("TAG", "saveWalletForGoal:=== ${error.message}")
                        trySend(Result.failure(Exception("Failed to update addAmount: ${error.message}")))
                        close()
                    }
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Result.failure(Exception("Database error: ${error.message}")))
                close()
            }
        }

        goalRef.child(ADD_AMOUNT).addListenerForSingleValueEvent(listener)
        awaitClose {
            goalRef.child(ADD_AMOUNT).removeEventListener(listener)
        }
    }
}