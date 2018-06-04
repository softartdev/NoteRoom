package com.softartdev.noteroom.ui.splash

import com.softartdev.noteroom.ui.base.MvpView

interface SplashView : MvpView {
    fun navSignIn()
    fun navMain()
    fun showError(message: String?)
}
