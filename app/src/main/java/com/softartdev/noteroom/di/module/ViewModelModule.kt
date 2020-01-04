package com.softartdev.noteroom.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.softartdev.noteroom.di.ViewModelKey
import com.softartdev.noteroom.model.NoteRoomViewModelFactory
import com.softartdev.noteroom.ui.main.MainViewModel
import com.softartdev.noteroom.ui.note.NoteViewModel
import com.softartdev.noteroom.ui.security.SecurityViewModel
import com.softartdev.noteroom.ui.security.change.ChangeViewModel
import com.softartdev.noteroom.ui.security.confirm.ConfirmViewModel
import com.softartdev.noteroom.ui.security.enter.EnterViewModel
import com.softartdev.noteroom.ui.signin.SignInViewModel
import com.softartdev.noteroom.ui.splash.SplashViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindSplashViewModel(splashViewModel: SplashViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SignInViewModel::class)
    abstract fun bindSignInViewModel(signInViewModel: SignInViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NoteViewModel::class)
    abstract fun bindNoteViewModel(noteViewModel: NoteViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SecurityViewModel::class)
    abstract fun bindSecurityViewModel(securityViewModel: SecurityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EnterViewModel::class)
    abstract fun bindEnterViewModel(enterViewModel: EnterViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ConfirmViewModel::class)
    abstract fun bindConfirmViewModel(confirmViewModel: ConfirmViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChangeViewModel::class)
    abstract fun bindChangeViewModel(changeViewModel: ChangeViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: NoteRoomViewModelFactory): ViewModelProvider.Factory
}
