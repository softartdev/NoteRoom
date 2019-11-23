package com.softartdev.noteroom.model

sealed class SignInResult {
    object NavMain : SignInResult()
    object ShowEmptyPassError : SignInResult()
    object ShowIncorrectPassError : SignInResult()
    object HideError : SignInResult()
    data class ShowProgress(val show: Boolean) : SignInResult()
    data class ShowError(val error: Throwable) : SignInResult()
}