package com.jrProfessor.todoapp.dagger

import androidx.lifecycle.ViewModel
import com.jrProfessor.todoapp.viewmodel.AuthenticationViewModel
import com.jrProfessor.todoapp.viewmodel.HomeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(AuthenticationViewModel::class)
    abstract fun authenticationViewModel(viewModel: AuthenticationViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun homeViewModel(viewModel: HomeViewModel):ViewModel
}