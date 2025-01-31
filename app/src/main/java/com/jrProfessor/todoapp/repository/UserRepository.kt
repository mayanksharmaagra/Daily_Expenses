package com.jrProfessor.todoapp.repository

import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.jrProfessor.todoapp.model.ExpensesModel
import com.jrProfessor.todoapp.model.User
import com.jrProfessor.todoapp.utils.AppUtils.DB_NAME
import com.jrProfessor.todoapp.utils.AppUtils.EXPENSES_TABLE
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
        databaseReference.child(EXPENSES_TABLE).child(uid)
            .push() // Generates a unique key for each entry
            .setValue(expenses)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess(true)

                } else {
                    onError(false, "Database Error: ${task.exception?.message}")
                }
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
                        val key = expenseSnapshot.key // Get unique key of each expense

                        if (expense != null && key != null) {
                            expense.id = key // Assign the unique key to the expense
                            expensesList.add(expense) // Add the expense to the list
                        }
                    }

                    onSuccess(expensesList)
                }

                override fun onCancelled(error: DatabaseError) {
                    onError(false, "Database Error: ${error.message}")
                }
            })
    }

}