package com.softartdev.noteroom.di.component

import com.softartdev.noteroom.di.PerActivity
import com.softartdev.noteroom.di.module.ActivityModule
import com.softartdev.noteroom.ui.base.BaseActivity
import com.softartdev.noteroom.ui.main.MainActivity
import com.softartdev.noteroom.ui.note.NoteActivity
import com.softartdev.noteroom.ui.signin.SignInActivity
import com.softartdev.noteroom.ui.splash.SplashActivity
import dagger.Subcomponent

@PerActivity
@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {

    fun inject(baseActivity: BaseActivity)

    fun inject(mainActivity: MainActivity)

    fun inject(signInActivity: SignInActivity)

    fun inject(splashActivity: SplashActivity)

    fun inject(noteActivity: NoteActivity)
}
