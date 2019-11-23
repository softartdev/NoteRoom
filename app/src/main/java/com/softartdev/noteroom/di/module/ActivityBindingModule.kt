package com.softartdev.noteroom.di.module

import com.softartdev.noteroom.di.PerActivity
import com.softartdev.noteroom.ui.main.MainActivity
import com.softartdev.noteroom.ui.note.NoteActivity
import com.softartdev.noteroom.ui.signin.SignInActivity
import com.softartdev.noteroom.ui.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @PerActivity
    @ContributesAndroidInjector()
    abstract fun splashActivity(): SplashActivity

    @PerActivity
    @ContributesAndroidInjector()
    abstract fun signInActivity(): SignInActivity

    @PerActivity
    @ContributesAndroidInjector()
    abstract fun mainActivity(): MainActivity

    @PerActivity
    @ContributesAndroidInjector()
    abstract fun noteActivity(): NoteActivity

}