package com.jrProfessor.todoapp.dagger

import com.jrProfessor.todoapp.repository.DatabaseRepository
import com.jrProfessor.todoapp.repository.DatabaseRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideDatabaseRepository(databaseRepositoryImpl: DatabaseRepositoryImpl): DatabaseRepository {
        return databaseRepositoryImpl
    }
}