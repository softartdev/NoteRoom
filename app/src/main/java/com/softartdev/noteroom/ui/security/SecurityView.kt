package com.softartdev.noteroom.ui.security

import android.text.Editable
import com.softartdev.noteroom.ui.base.MvpView

interface SecurityView : MvpView {
    fun showEncryptEnable(encryption: Boolean)
    fun showPasswordDialog()
    fun showSetPasswordDialog()
    fun showChangePasswordDialog()

    fun showError(message: String?)

    interface DialogDirector {
        val textString: Editable
        fun showIncorrectPasswordError()
        fun showEmptyPasswordError()
        fun showPasswordsNoMatchError()
        fun hideError()
    }
}
