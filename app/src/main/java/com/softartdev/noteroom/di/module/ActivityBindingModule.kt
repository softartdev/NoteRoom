package com.softartdev.noteroom.di.module

import com.softartdev.noteroom.di.CryptScope
import com.softartdev.noteroom.di.NoteScope
import com.softartdev.noteroom.ui.main.MainActivity
import com.softartdev.noteroom.ui.note.NoteActivity
import com.softartdev.noteroom.ui.signin.SignInActivity
import com.softartdev.noteroom.ui.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @CryptScope
    @ContributesAndroidInjector
    abstract fun splashActivity(): SplashActivity

    @CryptScope
    @ContributesAndroidInjector
    abstract fun signInActivity(): SignInActivity

    @NoteScope
    @ContributesAndroidInjector
    abstract fun mainActivity(): MainActivity

    @NoteScope
    @ContributesAndroidInjector
    abstract fun noteActivity(): NoteActivity

}