package com.softartdev.noteroom.ui.signin

import com.softartdev.noteroom.ui.base.ResultFactory

sealed class SignInResult {
    object ShowProgress : SignInResult()
    object NavMain : SignInResult()
    object ShowEmptyPassError : SignInResult()
    object ShowIncorrectPassError : SignInResult()
    data class ShowError(val error: Throwable) : SignInResult()

    class Factory : ResultFactory<SignInResult>() {
        override val loadingResult = ShowProgress
        override fun errorResult(throwable: Throwable) = ShowError(throwable)
    }
}
