package com.jrProfessor.todoapp.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PassbookDto::class], version = 1, exportSchema = false)
abstract class DatabaseBuilder : RoomDatabase() {
    abstract fun getDatabase(): PassbookDao
}