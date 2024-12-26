package com.jrProfessor.todoapp.roomdb

import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PassbookDao {
    @Insert
    suspend fun insertEntry(entry: PassbookDto)

    @Query("SELECT * FROM PassbookDto")
    suspend fun getUserData(): MutableList<PassbookDto>
}