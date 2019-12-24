package com.softartdev.noteroom.ui.security.confirm

sealed class ConfirmResult {
    object Success: ConfirmResult()
    object PasswordsNoMatchError: ConfirmResult()
    object EmptyPasswordError: ConfirmResult()
    data class Error(val message: String?): ConfirmResult()
}