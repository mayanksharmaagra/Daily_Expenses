package com.jrProfessor.todoapp.roomdb

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Insert
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext context: Context): DatabaseBuilder {
        return Room.databaseBuilder(context, DatabaseBuilder::class.java, "passbook_user").build()
    }

    @Provides
    fun providePassbookDao(database: DatabaseBuilder): PassbookDao {
        return database.getDatabase()
    }
}