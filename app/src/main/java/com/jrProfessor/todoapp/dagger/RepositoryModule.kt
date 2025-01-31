package com.jrProfessor.todoapp.dagger

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.jrProfessor.todoapp.TodoApplication
import com.jrProfessor.todoapp.repository.DatabaseRepository
import com.jrProfessor.todoapp.repository.DatabaseRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun provideDatabaseRepository(databaseRepositoryImpl: DatabaseRepositoryImpl): DatabaseRepository {
        return databaseRepositoryImpl
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(context: TodoApplication): SharedPreferences {
        return context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

}