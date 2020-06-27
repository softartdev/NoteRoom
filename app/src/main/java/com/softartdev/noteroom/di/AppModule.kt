package com.softartdev.noteroom.di

import com.softartdev.noteroom.shared.data.CryptUseCase
import com.softartdev.noteroom.shared.data.NoteUseCase
import com.softartdev.noteroom.shared.data.SafeRepo
import com.softartdev.noteroom.ui.main.MainActivity
import com.softartdev.noteroom.ui.main.MainViewModel
import com.softartdev.noteroom.ui.note.NoteActivity
import com.softartdev.noteroom.ui.note.NoteViewModel
import com.softartdev.noteroom.ui.settings.SettingsFragment
import com.softartdev.noteroom.ui.settings.SettingsViewModel
import com.softartdev.noteroom.ui.settings.security.change.ChangePasswordDialog
import com.softartdev.noteroom.ui.settings.security.change.ChangeViewModel
import com.softartdev.noteroom.ui.settings.security.confirm.ConfirmPasswordDialog
import com.softartdev.noteroom.ui.settings.security.confirm.ConfirmViewModel
import com.softartdev.noteroom.ui.settings.security.enter.EnterPasswordDialog
import com.softartdev.noteroom.ui.settings.security.enter.EnterViewModel
import com.softartdev.noteroom.ui.signin.SignInActivity
import com.softartdev.noteroom.ui.signin.SignInViewModel
import com.softartdev.noteroom.ui.splash.SplashActivity
import com.softartdev.noteroom.ui.splash.SplashViewModel
import com.softartdev.noteroom.ui.title.EditTitleDialog
import com.softartdev.noteroom.ui.title.EditTitleViewModel
import com.softartdev.noteroom.util.PreferencesHelper
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { PreferencesHelper(get()) }
    single { SafeRepo(get()) }
    single { CryptUseCase(get()) }
    single { NoteUseCase(get()) }
}

val mvvmModule = module {
    scope<SplashActivity> {
        viewModel { SplashViewModel(get()) }
    }
    scope<SignInActivity> {
        viewModel { SignInViewModel(get()) }
    }
    scope<MainActivity> {
        viewModel { MainViewModel(get()) }
    }
    scope<NoteActivity> {
        viewModel { NoteViewModel(get()) }
    }
    scope<EditTitleDialog> {
        viewModel { EditTitleViewModel(get()) }
    }
    scope<SettingsFragment> {
        viewModel { SettingsViewModel(get()) }
    }
    scope<EnterPasswordDialog> {
        viewModel { EnterViewModel(get()) }
    }
    scope<ConfirmPasswordDialog> {
        viewModel { ConfirmViewModel(get()) }
    }
    scope<ChangePasswordDialog> {
        viewModel { ChangeViewModel(get()) }
    }
}