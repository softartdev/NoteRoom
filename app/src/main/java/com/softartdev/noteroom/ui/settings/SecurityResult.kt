package com.softartdev.noteroom.ui.settings

import com.softartdev.noteroom.ui.base.ResultFactory

sealed class SecurityResult {
    data class EncryptEnable(val encryption: Boolean) : SecurityResult()
    object PasswordDialog : SecurityResult()
    object SetPasswordDialog : SecurityResult()
    object ChangePasswordDialog : SecurityResult()
    data class Error(val message: String?) : SecurityResult()

    class Factory : ResultFactory<SecurityResult>() {
        override fun errorResult(throwable: Throwable) = Error(throwable.message)
    }
}