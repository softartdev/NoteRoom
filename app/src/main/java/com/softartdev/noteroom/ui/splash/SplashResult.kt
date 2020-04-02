package com.softartdev.noteroom.ui.splash

import com.softartdev.noteroom.ui.base.ResultFactory

sealed class SplashResult {
    object NavSignIn : SplashResult()
    object NavMain : SplashResult()
    data class ShowError(val message: String?) : SplashResult()

    class Factory : ResultFactory<SplashResult>() {
        override fun errorResult(throwable: Throwable) = ShowError(throwable.message)
    }
}