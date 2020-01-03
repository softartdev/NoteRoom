package com.softartdev.noteroom.ui.security.enter

sealed class EnterResult {
    object Success: EnterResult()
    object EmptyPasswordError: EnterResult()
    object IncorrectPasswordError: EnterResult()
    data class Error(val message: String?): EnterResult()
}