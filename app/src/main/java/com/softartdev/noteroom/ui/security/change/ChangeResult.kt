package com.softartdev.noteroom.ui.security.change

sealed class ChangeResult {
    object Success: ChangeResult()
    object OldEmptyPasswordError: ChangeResult()
    object NewEmptyPasswordError: ChangeResult()
    object PasswordsNoMatchError: ChangeResult()
    object IncorrectPasswordError: ChangeResult()
    data class Error(val message: String?): ChangeResult()
}
