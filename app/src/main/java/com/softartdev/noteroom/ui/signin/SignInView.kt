package com.softartdev.noteroom.ui.signin

import com.softartdev.noteroom.ui.base.MvpView

interface SignInView : MvpView {
    fun navMain()
    fun showEmptyPassError()
    fun showIncorrectPassError()
    fun hideError()
}
