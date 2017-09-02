package com.softartdev.notecrypt.ui.settings.security

import com.softartdev.notecrypt.ui.base.MvpView

internal interface SecurityView : MvpView {
    fun showEncryptEnable(encryption: Boolean)
    fun showPasswordDialog()
    fun showSetPasswordDialog()
    fun showChangePasswordDialog()

    fun showError(message: String?)

    interface DialogDirector {
        val textString: String?
        fun showIncorrectPasswordError()
        fun showEmptyPasswordError()
        fun showPasswordsNoMatchError()
        fun hideError()
    }
}
