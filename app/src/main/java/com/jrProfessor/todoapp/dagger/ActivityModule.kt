package com.jrProfessor.todoapp.dagger

import com.jrProfessor.todoapp.screen.home.HomeActivity
import com.jrProfessor.todoapp.screen.welcome.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.hilt.android.scopes.ActivityScoped

@Module
abstract class ActivityModule {

    @ActivityScoped
    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity
    @ActivityScoped
    @ContributesAndroidInjector
    abstract fun homeActivity(): HomeActivity
}