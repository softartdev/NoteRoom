package com.softartdev.noteroom.ui.splash

sealed class SplashResult {
    object NavSignIn : SplashResult()
    object NavMain : SplashResult()
    data class ShowError(val message: String?) : SplashResult()
}