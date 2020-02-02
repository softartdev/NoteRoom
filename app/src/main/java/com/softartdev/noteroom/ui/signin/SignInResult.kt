package com.softartdev.noteroom.ui.signin

sealed class SignInResult {
    object ShowProgress : SignInResult()
    object NavMain : SignInResult()
    object ShowEmptyPassError : SignInResult()
    object ShowIncorrectPassError : SignInResult()
    data class ShowError(val error: Throwable) : SignInResult()
}
