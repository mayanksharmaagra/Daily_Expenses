package com.jrProfessor.todoapp.dagger

import com.jrProfessor.todoapp.TodoApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        FirebaseModule::class,
        RepositoryModule::class,
        ViewModelModule::class,
        ActivityModule::class
    ]
)
interface AppComponent : AndroidInjector<TodoApplication> {
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: TodoApplication): Builder
        fun build(): AppComponent
    }
}