package com.jrProfessor.todoapp.dagger

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object FirebaseModule {
    @Provides
    @Singleton
    fun provideFirebaseAuth():FirebaseAuth{
        return FirebaseAuth.getInstance()
    }
    @Provides
    @Singleton
    fun provideFirebaseDatabase():FirebaseDatabase{
        return FirebaseDatabase.getInstance()
    }
}