package com.softartdev.noteroom.model

sealed class SplashResult {
    object NavSignIn : SplashResult()
    object NavMain : SplashResult()
    data class ShowError(val message: String?) : SplashResult()
}