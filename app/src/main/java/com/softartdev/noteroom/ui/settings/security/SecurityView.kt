package com.softartdev.noteroom.ui.settings.security

import com.softartdev.noteroom.ui.base.MvpView

interface SecurityView : MvpView {
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
