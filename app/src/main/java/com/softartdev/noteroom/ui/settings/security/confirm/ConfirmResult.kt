package com.softartdev.noteroom.ui.settings.security.confirm

import com.softartdev.noteroom.ui.base.ResultFactory

sealed class ConfirmResult {
    object Loading: ConfirmResult()
    object Success: ConfirmResult()
    object PasswordsNoMatchError: ConfirmResult()
    object EmptyPasswordError: ConfirmResult()
    data class Error(val message: String?): ConfirmResult()

    class Factory : ResultFactory<ConfirmResult>() {
        override val loadingResult = Loading
        override fun errorResult(throwable: Throwable) = Error(throwable.message)
    }
}