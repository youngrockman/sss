package com.example.shoesapptest.di

import com.example.shoesapptest.data.local.DataStore
import com.example.shoesapptest.data.remote.network.AuthInterceptor
import com.example.shoesapptest.data.remote.network.RetrofitClient
import com.example.shoesapptest.data.repository.AuthRepository
import com.example.shoesapptest.domain.usecase.AuthUseCase
import com.example.shoesapptest.screen.ForSneakers.SneakersViewModel
import com.example.shoesapptest.screen.RegistrationScreen.RegistrationViewModel
import com.example.shoesapptest.screen.SignInScreen.SignInViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModules = module {

    single { DataStore(get()) }
    single { AuthInterceptor(get()) }
    single { RetrofitClient(get()) }
    single { get<RetrofitClient>().authService }

    single<AuthRepository> { AuthRepository(get()) }
    single { AuthUseCase(get(), get()) }

    viewModel { RegistrationViewModel(get()) }
    viewModel { SignInViewModel(get()) }
    viewModel { SneakersViewModel(get()) }
}