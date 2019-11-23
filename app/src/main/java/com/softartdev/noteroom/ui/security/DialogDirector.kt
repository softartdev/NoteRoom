package com.softartdev.noteroom.ui.security

import android.text.Editable

interface DialogDirector {
    val textString: Editable
    fun showIncorrectPasswordError()
    fun showEmptyPasswordError()
    fun showPasswordsNoMatchError()
    fun hideError()
}