package com.softartdev.noteroom.ui.settings.security.change

import com.softartdev.noteroom.ui.base.ResultFactory

sealed class ChangeResult {
    object Loading: ChangeResult()
    object Success: ChangeResult()
    object OldEmptyPasswordError: ChangeResult()
    object NewEmptyPasswordError: ChangeResult()
    object PasswordsNoMatchError: ChangeResult()
    object IncorrectPasswordError: ChangeResult()
    data class Error(val message: String?): ChangeResult()

    class Factory : ResultFactory<ChangeResult>() {
        override val loadingResult = Loading
        override fun errorResult(throwable: Throwable) = Error(throwable.message)
    }
}
