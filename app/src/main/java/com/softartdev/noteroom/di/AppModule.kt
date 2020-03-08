package com.softartdev.noteroom.di

import com.softartdev.noteroom.data.CryptUseCase
import com.softartdev.noteroom.data.NoteUseCase
import com.softartdev.noteroom.data.SafeRepo
import com.softartdev.noteroom.ui.main.MainViewModel
import com.softartdev.noteroom.ui.note.NoteViewModel
import com.softartdev.noteroom.ui.settings.SettingsViewModel
import com.softartdev.noteroom.ui.settings.security.change.ChangeViewModel
import com.softartdev.noteroom.ui.settings.security.confirm.ConfirmViewModel
import com.softartdev.noteroom.ui.settings.security.enter.EnterViewModel
import com.softartdev.noteroom.ui.signin.SignInViewModel
import com.softartdev.noteroom.ui.splash.SplashViewModel
import com.softartdev.noteroom.ui.title.EditTitleViewModel
import com.softartdev.noteroom.util.PreferencesHelper
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { PreferencesHelper(get()) }
    single { SafeRepo(get()) }
    single { CryptUseCase(get(), get()) }
    single { NoteUseCase(get()) }
}

val mvvmModule = module {
    viewModel { SplashViewModel(get()) }
    viewModel { SignInViewModel(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { NoteViewModel(get()) }
    viewModel { EditTitleViewModel(get()) }
    viewModel { SettingsViewModel(get()) }
    viewModel { EnterViewModel(get()) }
    viewModel { ConfirmViewModel(get()) }
    viewModel { ChangeViewModel(get()) }
}